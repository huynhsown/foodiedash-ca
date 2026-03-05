package com.ute.foodiedash.interfaces.rest.promotion.dto;

import com.ute.foodiedash.domain.promotion.enums.DiscountAppliesTo;
import com.ute.foodiedash.domain.promotion.enums.PromotionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PromotionDiscountResponseDTO {
    private Long promotionId;
    private PromotionType type;
    private BigDecimal value;
    private BigDecimal maxDiscountAmount;
    private DiscountAppliesTo appliesTo;
}
