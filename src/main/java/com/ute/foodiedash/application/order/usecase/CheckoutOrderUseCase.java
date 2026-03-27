package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.order.command.CheckoutOrderCommand;
import com.ute.foodiedash.application.order.port.KitchenAvailabilityPort;
import com.ute.foodiedash.application.order.port.RouteCalculationPort;
import com.ute.foodiedash.application.order.query.CheckoutOrderResult;
import com.ute.foodiedash.application.order.query.RouteQueryResult;
import com.ute.foodiedash.application.paymentmethod.factory.PaymentPortFactory;
import com.ute.foodiedash.application.paymentmethod.port.PaymentPort;
import com.ute.foodiedash.application.promotion.query.PromotionPreviewResult;
import com.ute.foodiedash.application.promotion.service.PromotionCheckoutService;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.order.enums.OrderStatus;
import com.ute.foodiedash.domain.order.model.*;
import com.ute.foodiedash.domain.order.repository.OrderDeliveryRepository;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import com.ute.foodiedash.domain.order.repository.OrderPaymentRepository;
import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodCode;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CheckoutOrderUseCase {

    private final CartRepository cartRepository;
    private final PromotionCheckoutService promotionCheckoutService;

    private final KitchenAvailabilityPort kitchenAvailabilityPort;
    private final RouteCalculationPort routeCalculationPort;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final OrderDeliveryRepository orderDeliveryRepository;

    private final PaymentPortFactory paymentGatewayFactory;

    @Transactional
    public CheckoutOrderResult execute(CheckoutOrderCommand command) {
        Cart cart = cartRepository.findByUserId(command.customerId())
                .orElseThrow(() -> new BadRequestException("Cart not found"));
        Restaurant restaurant = restaurantRepository.findById(command.restaurantId())
                .orElseThrow(()-> new BadRequestException("Restaurant not found"));
        restaurant.ensureActive();
        if(!restaurant.hasCoordinates() || command.deliveryLat() == null || command.deliveryLng() == null) {
            throw new BadRequestException("Cannot checkout");
        }

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        if (!cart.getRestaurantId().equals(command.restaurantId())) {
            throw new BadRequestException("Cart belongs to another restaurant");
        }

        if (!kitchenAvailabilityPort.isKitchenAvailable(command.restaurantId())) {
            throw new BadRequestException("Kitchen is unavailable");
        }

        List<CartItem> activeCartItems = cart.getItems().stream()
                .filter(ci -> ci != null && !ci.isDeleted())
                .toList();

        BigDecimal subtotal = activeCartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        RouteQueryResult route = routeCalculationPort.calculateRoute(
                new Coordinate(restaurant.getLat(), restaurant.getLng()),
                new Coordinate(command.deliveryLat(), command.deliveryLng())
        );

        BigDecimal distanceInKm = BigDecimal.valueOf(route.distance());
        BigDecimal distanceInMeters = distanceInKm.multiply(BigDecimal.valueOf(1000));
        Integer etaInMinutes = 20 + route.etaInMinutes();
        BigDecimal deliveryFee = calculateDeliveryFee(distanceInKm);

        PromotionPreviewResult promotionPreview = promotionCheckoutService.previewForCheckout(
                command.voucherCode(),
                command.customerId(),
                command.restaurantId(),
                subtotal
        );
        if (promotionPreview != null && !promotionPreview.isValid()) {
            throw new BadRequestException(promotionPreview.message());
        }

        BigDecimal discount = promotionPreview != null ? promotionPreview.discount() : BigDecimal.ZERO;
        Long promotionId = promotionPreview != null ? promotionPreview.promotionId() : null;

        BigDecimal total = subtotal.add(deliveryFee).subtract(discount);
        if (total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Total amount must be greater than zero");
        }


        String orderCode = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 30).toUpperCase();
        Order order = Order.create(orderCode, command.customerId(), command.restaurantId(), deliveryFee);
        for (CartItem cartItem : activeCartItems) {
            order.addItem(toOrderItem(cartItem));
        }
        order.setDiscountAmount(discount);

        Order saved = orderRepository.save(order);
        Long orderId = saved.getId();

        OrderStatusHistory statusHistory = OrderStatusHistory.create(
                orderId,
                OrderStatus.PENDING,
                "Order created from checkout"
        );
        saved.addStatusHistory(statusHistory);

        if (promotionId != null && discount.compareTo(BigDecimal.ZERO) > 0) {
            OrderPromotion orderPromotion = OrderPromotion.create(
                    orderId,
                    promotionId,
                    promotionPreview.code(),
                    discount
            );
            saved.addPromotion(orderPromotion);
            promotionCheckoutService.reserve(promotionId, command.customerId(), orderId);
        }

        saved = orderRepository.save(saved);

        PaymentPort paymentPort = paymentGatewayFactory.get(command.paymentMethod());
        String paymentUrl = null;
        if (!command.paymentMethod().equals(PaymentMethodCode.COD)) {
            paymentUrl = paymentPort.createPaymentUrl(orderCode, order.getTotalAmount().longValue());
        }

        OrderPayment orderPayment = OrderPayment.create(
                orderId,
                command.paymentMethod()
        );
        orderPaymentRepository.save(orderPayment);

        OrderDelivery orderDelivery = OrderDelivery.create(
                orderId,
                command.deliveryAddress(),
                command.deliveryLat(),
                command.deliveryLng(),
                command.receiverName(),
                command.receiverPhone(),
                command.note()
        );

        orderDelivery.updateRouteInfo(distanceInKm, etaInMinutes, route.geometry());
        orderDeliveryRepository.save(orderDelivery);



        return new CheckoutOrderResult(
                saved.getId(),
                saved.getCode(),
                saved.getStatus().name(),
                subtotal,
                discount,
                deliveryFee,
                total,
                distanceInMeters,
                etaInMinutes,
                paymentUrl
        );
    }

    private OrderItem toOrderItem(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        OrderItem orderItem = OrderItem.create(
            cartItem.getMenuItemId(),
            cartItem.getName(),
            cartItem.getImageUrl(),
            cartItem.getQuantity(),
            cartItem.getUnitPrice(),
            cartItem.getNotes()
        );

        if (cartItem.getOptions() != null) {
            for (CartItemOption cartOption : cartItem.getOptions()) {
                OrderItemOption orderOption = toOrderItemOption(cartOption);
                if (orderOption != null) {
                    orderItem.addOption(orderOption);
                }
            }
        }

        return orderItem;
    }

    private OrderItemOption toOrderItemOption(CartItemOption cartOption) {
        if (cartOption == null) {
            return null;
        }

        OrderItemOption option = OrderItemOption.create(
            cartOption.getOptionId(),
            cartOption.getOptionName(),
            cartOption.getRequired(),
            cartOption.getMinValue(),
            cartOption.getMaxValue()
        );

        if (cartOption.getValues() != null) {
            for (CartItemOptionValue cartValue : cartOption.getValues()) {
                OrderItemOptionValue value = OrderItemOptionValue.create(
                    cartValue.getOptionValueId(),
                    cartValue.getOptionValueName(),
                    cartValue.getQuantity(),
                    cartValue.getExtraPrice()
                );
                option.addValue(value);
            }
        }

        option.validateSelection();
        return option;
    }

    private BigDecimal calculateDeliveryFee(BigDecimal distanceInKm) {
        BigDecimal baseFee = BigDecimal.valueOf(15000);
        BigDecimal perKm = BigDecimal.valueOf(3000);
        return baseFee.add(perKm.multiply(distanceInKm));
    }

}
