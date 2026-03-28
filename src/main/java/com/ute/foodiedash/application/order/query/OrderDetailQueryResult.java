package com.ute.foodiedash.application.order.query;

import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.model.OrderDelivery;
import com.ute.foodiedash.domain.order.model.OrderPayment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailQueryResult(
        Long orderId,
        String orderCode,
        String status,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal deliveryFee,
        BigDecimal total,
        LocalDateTime placedAt,
        LocalDateTime acceptedAt,
        LocalDateTime preparedAt,
        LocalDateTime cancelledAt,
        LocalDateTime completeAt,
        List<ItemResult> items,
        List<PromotionResult> promotions,
        List<StatusHistoryResult> statusHistories,
        PaymentResult payment,
        DeliveryResult delivery
) {
    public static OrderDetailQueryResult from(Order order, OrderPayment payment, OrderDelivery delivery) {
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
                order.getCompleteAt(),
                ItemResult.listFrom(order.getItems()),
                PromotionResult.listFrom(order.getPromotions()),
                StatusHistoryResult.listFromSorted(order.getStatusHistories()),
                PaymentResult.from(payment),
                DeliveryResult.from(delivery)
        );
    }
}
