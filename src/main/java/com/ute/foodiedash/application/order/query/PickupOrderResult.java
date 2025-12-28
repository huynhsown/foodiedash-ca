package com.ute.foodiedash.application.order.query;

import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.model.OrderDelivery;
import com.ute.foodiedash.domain.order.model.OrderPayment;

import java.time.Instant;
import java.util.List;

public record PickupOrderResult(
        Long orderId,
        String orderCode,
        String status,
        Instant pickedUpAt,
        List<ItemResult> items,
        DeliveryResult delivery,
        PaymentResult payment
) {
    public static PickupOrderResult from(Order order, OrderDelivery delivery, OrderPayment payment) {
        return new PickupOrderResult(
                order.getId(),
                order.getCode(),
                order.getStatus() != null ? order.getStatus().name() : null,
                delivery.getPickedUpAt(),
                ItemResult.listFrom(order.getItems()),
                DeliveryResult.from(delivery),
                PaymentResult.from(payment)
        );
    }
}
