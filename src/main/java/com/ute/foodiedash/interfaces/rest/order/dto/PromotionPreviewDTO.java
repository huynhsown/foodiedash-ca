package com.ute.foodiedash.interfaces.rest.order.dto;

import lombok.Data;

@Data
public class PromotionPreviewDTO {
    private String code;
    private boolean isValid;
    private Long discount;
    private String message;
    private Long promotionId;
}

