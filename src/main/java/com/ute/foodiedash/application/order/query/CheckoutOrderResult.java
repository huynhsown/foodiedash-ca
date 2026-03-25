package com.ute.foodiedash.application.order.query;

import java.math.BigDecimal;

public record CheckoutOrderResult(
        Long orderId,
        String orderCode,
        String status,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal deliveryFee,
        BigDecimal total,
        BigDecimal distanceInKm,
        Integer etaInMinutes
) {}
