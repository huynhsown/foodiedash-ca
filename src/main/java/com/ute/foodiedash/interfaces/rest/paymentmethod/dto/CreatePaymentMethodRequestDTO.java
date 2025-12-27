package com.ute.foodiedash.interfaces.rest.paymentmethod.dto;

import com.ute.foodiedash.domain.paymentmethod.enums.PaymentMethodType;
import lombok.Data;

@Data
public class CreatePaymentMethodRequestDTO {
    private String code;
    private String name;
    private PaymentMethodType type;
    private Boolean active;
}

