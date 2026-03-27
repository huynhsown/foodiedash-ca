package com.ute.foodiedash.application.order.command;

import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodCode;

import java.math.BigDecimal;

public record CheckoutOrderCommand(
        Long customerId,
        Long restaurantId,
        String deliveryAddress,
        BigDecimal deliveryLat,
        BigDecimal deliveryLng,
        String receiverName,
        String receiverPhone,
        String note,
        String voucherCode,
        PaymentMethodCode paymentMethod
) {}
