package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.order.command.PreviewOrderCommand;
import com.ute.foodiedash.application.order.port.KitchenAvailabilityPort;
import com.ute.foodiedash.application.order.port.RouteCalculationPort;
import com.ute.foodiedash.domain.order.model.Coordinate;
import com.ute.foodiedash.application.order.query.PriceBreakdownResult;
import com.ute.foodiedash.application.order.query.PreviewOrderItemOptionResult;
import com.ute.foodiedash.application.order.query.PreviewOrderItemOptionValueResult;
import com.ute.foodiedash.application.order.query.PreviewOrderItemResult;
import com.ute.foodiedash.application.order.query.PreviewOrderResult;
import com.ute.foodiedash.application.order.query.RouteQueryResult;
import com.ute.foodiedash.application.paymentmethod.query.PaymentMethodQueryResult;
import com.ute.foodiedash.application.promotion.query.PromotionPreviewResult;
import com.ute.foodiedash.application.promotion.service.PromotionCheckoutService;
import com.ute.foodiedash.domain.cart.model.Cart;
import com.ute.foodiedash.domain.cart.model.CartItem;
import com.ute.foodiedash.domain.cart.model.CartItemOption;
import com.ute.foodiedash.domain.cart.model.CartItemOptionValue;
import com.ute.foodiedash.domain.cart.repository.CartRepository;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.paymentmethod.repository.PaymentMethodRepository;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PreviewOrderUseCase {

    private final CartRepository cartRepository;
    private final PromotionCheckoutService promotionCheckoutService;

    private final KitchenAvailabilityPort kitchenAvailabilityPort;
    private final RouteCalculationPort routeCalculationPort;
    private final RestaurantRepository restaurantRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Transactional(readOnly = true)
    public PreviewOrderResult execute(PreviewOrderCommand command) {
        Cart cart = cartRepository.findByUserId(command.customerId())
                .orElseThrow(() -> new BadRequestException("Cart not found"));

        Restaurant restaurant = restaurantRepository.findById(command.restaurantId())
                .orElseThrow(() -> new BadRequestException("Restaurant not found"));
        restaurant.ensureActive();

        if (!restaurant.hasCoordinates() || command.deliveryLat() == null || command.deliveryLng() == null) {
            throw new BadRequestException("Cannot preview order");
        }

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        if (!cart.getRestaurantId().equals(command.restaurantId())) {
            throw new BadRequestException("Cart belongs to another restaurant");
        }

        boolean kitchenAvailable = kitchenAvailabilityPort.isKitchenAvailable(command.restaurantId());

        BigDecimal baseSubtotal = cart.getItems().stream()
                .filter(ci -> ci != null && !ci.isDeleted())
                .map(ci -> ci.getUnitPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal extrasSubtotal = cart.getItems().stream()
                .filter(ci -> ci != null && !ci.isDeleted())
                .map(CartItem::getTotalExtraPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal itemsSubtotal = cart.getItems().stream()
                .filter(ci -> ci != null && !ci.isDeleted())
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal subtotal = itemsSubtotal;

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
        BigDecimal discount = promotionPreview != null ? promotionPreview.discount() : BigDecimal.ZERO;

        BigDecimal total = subtotal.add(deliveryFee).subtract(discount);
        boolean totalPositive = total.compareTo(BigDecimal.ZERO) > 0;

        PriceBreakdownResult priceBreakDown = new PriceBreakdownResult(
                baseSubtotal,
                extrasSubtotal,
                itemsSubtotal,
                discount,
                deliveryFee,
                total
        );

        List<PaymentMethodQueryResult> paymentMethods = paymentMethodRepository.findAllActive()
                .stream()
                .map(PaymentMethodQueryResult::from)
                .toList();

        boolean promotionValid = promotionPreview != null && promotionPreview.isValid();
        boolean canCheckout = kitchenAvailable && totalPositive && promotionValid;

        return new PreviewOrderResult(
                toItemResults(cart.getItems()),
                promotionPreview,
                command.paymentMethod(),
                priceBreakDown,
                total,
                distanceInMeters,
                etaInMinutes,
                canCheckout,
                paymentMethods
        );
    }

    private List<PreviewOrderItemResult> toItemResults(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return List.of();
        }
        List<PreviewOrderItemResult> results = new ArrayList<>(cartItems.size());
        for (CartItem item : cartItems) {
            if (item == null || item.isDeleted()) {
                continue;
            }
            results.add(toItemResult(item));
        }
        return results;
    }

    private PreviewOrderItemResult toItemResult(CartItem item) {
        return new PreviewOrderItemResult(
                item.getMenuItemId(),
                item.getName(),
                item.getImageUrl(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice(),
                item.getNotes(),
                toOptionResults(item.getOptions())
        );
    }

    private List<PreviewOrderItemOptionResult> toOptionResults(List<CartItemOption> options) {
        if (options == null || options.isEmpty()) {
            return List.of();
        }
        List<PreviewOrderItemOptionResult> results = new ArrayList<>(options.size());
        for (CartItemOption option : options) {
            if (option == null || option.isDeleted()) {
                continue;
            }
            results.add(new PreviewOrderItemOptionResult(
                    option.getOptionId(),
                    option.getOptionName(),
                    option.getRequired(),
                    option.getMinValue(),
                    option.getMaxValue(),
                    toValueResults(option.getValues())
            ));
        }
        return results;
    }

    private List<PreviewOrderItemOptionValueResult> toValueResults(List<CartItemOptionValue> values) {
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        List<PreviewOrderItemOptionValueResult> results = new ArrayList<>(values.size());
        for (CartItemOptionValue value : values) {
            if (value == null || value.isDeleted()) {
                continue;
            }
            results.add(new PreviewOrderItemOptionValueResult(
                    value.getOptionValueId(),
                    value.getOptionValueName(),
                    value.getQuantity(),
                    value.getExtraPrice(),
                    value.getTotalExtraPrice()
            ));
        }
        return results;
    }

    private BigDecimal calculateDeliveryFee(BigDecimal distanceInKm) {
        BigDecimal baseFee = BigDecimal.valueOf(15000);
        BigDecimal perKm = BigDecimal.valueOf(3000);
        return baseFee.add(perKm.multiply(distanceInKm));
    }
}

