package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PreviewOrderRequestDTO {
    private Long customerId;
    private Long restaurantId;
    private BigDecimal deliveryLat;
    private BigDecimal deliveryLng;
    private String voucherCode;
}

