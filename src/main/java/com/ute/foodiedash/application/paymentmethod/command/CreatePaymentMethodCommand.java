package com.ute.foodiedash.application.paymentmethod.command;

import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodType;

public record CreatePaymentMethodCommand(
        String code,
        String name,
        PaymentMethodType type,
        Boolean active
) {}

