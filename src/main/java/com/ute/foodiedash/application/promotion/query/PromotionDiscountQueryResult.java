package com.ute.foodiedash.application.promotion.query;

import com.ute.foodiedash.domain.promotion.enums.DiscountAppliesTo;
import com.ute.foodiedash.domain.promotion.enums.PromotionType;

import java.math.BigDecimal;

public record PromotionDiscountQueryResult(
    Long promotionId,
    PromotionType type,
    BigDecimal value,
    BigDecimal maxDiscountAmount,
    DiscountAppliesTo appliesTo
) {}
