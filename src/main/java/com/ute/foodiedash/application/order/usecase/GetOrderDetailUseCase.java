package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.order.query.OrderDetailQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.common.exception.UnauthorizedException;
import com.ute.foodiedash.domain.order.model.Coordinate;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.model.OrderDelivery;
import com.ute.foodiedash.domain.order.model.OrderItem;
import com.ute.foodiedash.domain.order.model.OrderItemOption;
import com.ute.foodiedash.domain.order.model.OrderItemOptionValue;
import com.ute.foodiedash.domain.order.model.OrderPayment;
import com.ute.foodiedash.domain.order.model.OrderPromotion;
import com.ute.foodiedash.domain.order.model.OrderStatusHistory;
import com.ute.foodiedash.domain.order.repository.OrderDeliveryRepository;
import com.ute.foodiedash.domain.order.repository.OrderPaymentRepository;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GetOrderDetailUseCase {
    private final OrderRepository orderRepository;
    private final OrderPaymentRepository orderPaymentRepository;
    private final OrderDeliveryRepository orderDeliveryRepository;

    @Transactional(readOnly = true)
    public OrderDetailQueryResult execute(Long customerId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (!Objects.equals(order.getCustomerId(), customerId)) {
            throw new UnauthorizedException("You are not allowed to access this order");
        }

        OrderPayment payment = orderPaymentRepository.findByOrderId(orderId).orElse(null);
        OrderDelivery delivery = orderDeliveryRepository.findByOrderId(orderId).orElse(null);

        return new OrderDetailQueryResult(
                order.getId(),
                order.getCode(),
                order.getStatus() != null ? order.getStatus().name() : null,
                order.getSubtotalAmount(),
                order.getDiscountAmount(),
                order.getDeliveryFee(),
                order.getTotalAmount(),
                order.getPlacedAt(),
                order.getAcceptedAt(),
                order.getPreparedAt(),
                order.getCancelledAt(),
                mapItems(order.getItems()),
                mapPromotions(order.getPromotions()),
                mapStatusHistories(order.getStatusHistories()),
                mapPayment(payment),
                mapDelivery(delivery)
        );
    }

    private List<OrderDetailQueryResult.ItemResult> mapItems(List<OrderItem> items) {
        if (items == null) {
            return List.of();
        }
        return items.stream()
                .map(this::mapItem)
                .toList();
    }

    private OrderDetailQueryResult.ItemResult mapItem(OrderItem item) {
        return new OrderDetailQueryResult.ItemResult(
                item.getId(),
                item.getMenuItemId(),
                item.getName(),
                item.getImageUrl(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice(),
                item.getNotes(),
                mapOptions(item.getOptions())
        );
    }

    private List<OrderDetailQueryResult.OptionResult> mapOptions(List<OrderItemOption> options) {
        if (options == null) {
            return List.of();
        }
        return options.stream()
                .map(this::mapOption)
                .toList();
    }

    private OrderDetailQueryResult.OptionResult mapOption(OrderItemOption option) {
        return new OrderDetailQueryResult.OptionResult(
                option.getId(),
                option.getOptionId(),
                option.getOptionName(),
                option.getRequired(),
                option.getMinValue(),
                option.getMaxValue(),
                mapOptionValues(option.getValues())
        );
    }

    private List<OrderDetailQueryResult.OptionValueResult> mapOptionValues(List<OrderItemOptionValue> values) {
        if (values == null) {
            return List.of();
        }
        return values.stream()
                .map(this::mapOptionValue)
                .toList();
    }

    private OrderDetailQueryResult.OptionValueResult mapOptionValue(OrderItemOptionValue value) {
        return new OrderDetailQueryResult.OptionValueResult(
                value.getId(),
                value.getOptionValueId(),
                value.getOptionValueName(),
                value.getQuantity(),
                value.getExtraPrice()
        );
    }

    private List<OrderDetailQueryResult.PromotionResult> mapPromotions(List<OrderPromotion> promotions) {
        if (promotions == null) {
            return List.of();
        }
        return promotions.stream()
                .map(p -> new OrderDetailQueryResult.PromotionResult(
                        p.getPromotionId(),
                        p.getPromotionCode(),
                        p.getDiscountAmount()
                ))
                .toList();
    }

    private List<OrderDetailQueryResult.StatusHistoryResult> mapStatusHistories(List<OrderStatusHistory> histories) {
        if (histories == null) {
            return List.of();
        }
        return histories.stream()
                .sorted(Comparator.comparing(OrderStatusHistory::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(h -> new OrderDetailQueryResult.StatusHistoryResult(
                        h.getId(),
                        h.getStatus() != null ? h.getStatus().name() : null,
                        h.getNote(),
                        h.getCreatedAt()
                ))
                .toList();
    }

    private OrderDetailQueryResult.PaymentResult mapPayment(OrderPayment payment) {
        if (payment == null) {
            return null;
        }
        return new OrderDetailQueryResult.PaymentResult(
                payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null,
                payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : null,
                payment.getTransactionId(),
                payment.getPaidAt(),
                payment.getRefundedAt()
        );
    }

    private OrderDetailQueryResult.DeliveryResult mapDelivery(OrderDelivery delivery) {
        if (delivery == null) {
            return null;
        }
        return new OrderDetailQueryResult.DeliveryResult(
                delivery.getAddress(),
                delivery.getLat(),
                delivery.getLng(),
                delivery.getReceiverName(),
                delivery.getReceiverPhone(),
                delivery.getNote(),
                delivery.getDistanceKm(),
                delivery.getEtaMinutes(),
                mapGeometry(delivery.getGeometry())
        );
    }

    private List<OrderDetailQueryResult.CoordinateResult> mapGeometry(List<Coordinate> geometry) {
        if (geometry == null) {
            return List.of();
        }
        return geometry.stream()
                .map(c -> new OrderDetailQueryResult.CoordinateResult(c.lat(), c.lng()))
                .toList();
    }
}
