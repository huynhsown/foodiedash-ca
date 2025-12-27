package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSummaryResponseDTO {
    private Long orderId;
    private String orderCode;
    private String status;
    private Long subtotal;
    private Long discount;
    private Long deliveryFee;
    private Long total;
    private LocalDateTime placedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime preparedAt;
    private LocalDateTime cancelledAt;
}

