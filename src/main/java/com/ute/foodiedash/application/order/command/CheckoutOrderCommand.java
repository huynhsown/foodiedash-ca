package com.ute.foodiedash.application.order.command;

import java.math.BigDecimal;

public record CheckoutOrderCommand(
        Long customerId,
        Long restaurantId,
        String deliveryAddress,
        BigDecimal deliveryLat,
        BigDecimal deliveryLng,
        String voucherCode,
        String paymentMethod
) {}
