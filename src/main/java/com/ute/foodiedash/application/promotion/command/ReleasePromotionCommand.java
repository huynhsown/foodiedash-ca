package com.ute.foodiedash.application.promotion.command;

public record ReleasePromotionCommand(
    Long promotionId,
    Long orderId
) {}
