package com.ute.foodiedash.application.promotion.query;

import java.math.BigDecimal;

public record PromotionPreviewResult(
        String code,
        boolean isValid,
        BigDecimal discount,
        String message,
        Long promotionId
) {}

