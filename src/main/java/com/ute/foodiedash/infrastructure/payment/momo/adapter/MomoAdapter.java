package com.ute.foodiedash.infrastructure.payment.momo.adapter;

import com.ute.foodiedash.application.paymentmethod.port.PaymentPort;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodCode;
import com.ute.foodiedash.infrastructure.payment.momo.config.MomoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MomoAdapter implements PaymentPort {

    private final MomoProperties props;

    @Override
    public PaymentMethodCode getCode() {
        return PaymentMethodCode.MOMO;
    }

    @Override
    public String createPaymentUrl(Long orderId, long amount) {
        throw new BadRequestException("Not implement yet");
    }
}
