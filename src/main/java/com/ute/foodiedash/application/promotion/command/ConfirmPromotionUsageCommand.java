package com.ute.foodiedash.application.promotion.command;

public record ConfirmPromotionUsageCommand(
    Long promotionId,
    Long orderId
) {}
