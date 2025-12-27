package com.ute.foodiedash.application.paymentmethod.port;

import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodCode;

public interface PaymentPort {
    PaymentMethodCode getCode();
    String createPaymentUrl(String orderCode, long amount);
}
