package com.ute.foodiedash.application.order.command;

import java.math.BigDecimal;

public record PreviewOrderCommand(
        Long customerId,
        Long restaurantId,
        BigDecimal deliveryLat,
        BigDecimal deliveryLng,
        String voucherCode,
        String paymentMethod
) {}

