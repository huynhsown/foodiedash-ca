package com.ute.foodiedash.interfaces.rest.promotion.dto;

import lombok.Data;

@Data
public class PromotionEligibilityResponseDTO {
    private boolean eligible;
    private Long promotionId;
    private String reason;
}
