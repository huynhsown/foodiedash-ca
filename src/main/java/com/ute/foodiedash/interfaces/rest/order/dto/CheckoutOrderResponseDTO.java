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
    private String paymentUrl;
    private String deliveryAddress;
    private Double deliveryLat;
    private Double deliveryLng;
    private String receiverName;
    private String receiverPhone;
    private String note;
}
