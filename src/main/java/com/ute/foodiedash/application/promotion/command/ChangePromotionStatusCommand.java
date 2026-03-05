package com.ute.foodiedash.application.promotion.command;

import com.ute.foodiedash.domain.promotion.enums.PromotionStatus;

public record ChangePromotionStatusCommand(
    PromotionStatus status
) {}
