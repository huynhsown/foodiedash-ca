package com.ute.foodiedash.application.promotion.query;

import com.ute.foodiedash.domain.promotion.enums.PromotionUsageStatus;

public record PromotionReservationQueryResult(
    Long promotionUsageId,
    Long promotionId,
    Long orderId,
    PromotionUsageStatus status
) {}
