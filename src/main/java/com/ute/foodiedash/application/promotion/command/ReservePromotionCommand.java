package com.ute.foodiedash.application.promotion.command;

public record ReservePromotionCommand(
    Long promotionId,
    Long userId,
    Long orderId
) {}
