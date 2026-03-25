package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CheckoutOrderResponseDTO {
    private Long orderId;
    private String orderCode;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal deliveryFee;
    private BigDecimal total;
    private BigDecimal distanceInKm;
    private Integer etaInMinutes;
}
