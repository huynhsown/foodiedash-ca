package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.Data;

@Data
public class CheckoutOrderResponseDTO {
    private Long orderId;
    private String orderCode;
    private String status;
    private Long subtotal;
    private Long discount;
    private Long deliveryFee;
    private Long total;
    private Long distanceMeters;
    private Integer etaInMinutes;
}
