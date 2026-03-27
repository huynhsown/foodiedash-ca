package com.ute.foodiedash.application.paymentmethod.command;

import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodType;

public record UpdatePaymentMethodCommand(
        String name,
        PaymentMethodType type,
        Boolean active
) {}

