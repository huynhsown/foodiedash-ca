package com.ute.foodiedash.interfaces.rest.promotion.dto;

import com.ute.foodiedash.domain.promotion.enums.PromotionUsageStatus;
import lombok.Data;

@Data
public class PromotionReservationResponseDTO {
    private Long promotionUsageId;
    private Long promotionId;
    private Long orderId;
    private PromotionUsageStatus status;
}
