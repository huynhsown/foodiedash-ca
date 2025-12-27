package com.ute.foodiedash.infrastructure.payment.cod.adapter;

import com.ute.foodiedash.application.paymentmethod.port.PaymentPort;
import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodCode;
import org.springframework.stereotype.Component;

@Component
public class CodeAdapter implements PaymentPort {
    @Override
    public PaymentMethodCode getCode() {
        return PaymentMethodCode.COD;
    }

    @Override
    public String createPaymentUrl(String orderCode, long amount) {
        return "";
    }
}
