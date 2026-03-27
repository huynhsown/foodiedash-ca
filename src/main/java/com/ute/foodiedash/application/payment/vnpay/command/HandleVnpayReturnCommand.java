package com.ute.foodiedash.application.payment.vnpay.command;

import java.util.Map;

public record HandleVnpayReturnCommand(
        Map<String, String> params
) {}

