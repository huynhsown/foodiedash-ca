package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CheckoutOrderRequestDTO {
    private Long customerId;
    private Long restaurantId;
    private String deliveryAddress;
    private BigDecimal deliveryLat;
    private BigDecimal deliveryLng;
    private String receiverName;
    private String receiverPhone;
    private String note;
    private String voucherCode;
    private String paymentMethod;
}
