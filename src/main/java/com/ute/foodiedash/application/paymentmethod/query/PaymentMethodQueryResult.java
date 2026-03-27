package com.ute.foodiedash.application.paymentmethod.query;

import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodCode;
import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodType;
import com.ute.foodiedash.domain.paymentmethod.model.PaymentMethod;

public record PaymentMethodQueryResult(
        Long id,
        PaymentMethodCode code,
        String name,
        PaymentMethodType type,
        boolean active
) {
    public static PaymentMethodQueryResult from(PaymentMethod pm) {
        if (pm == null) {
            return null;
        }
        return new PaymentMethodQueryResult(
                pm.getId(),
                pm.getCode(),
                pm.getName(),
                pm.getType(),
                pm.isActive()
        );
    }
}

