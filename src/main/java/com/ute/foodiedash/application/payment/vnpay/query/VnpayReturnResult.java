package com.ute.foodiedash.application.payment.vnpay.query;

public record VnpayReturnResult(
        boolean success,
        String message
) {}

