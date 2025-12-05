package com.ute.foodiedash.application.promotion.command;

import com.ute.foodiedash.domain.promotion.enums.EligibilityRule;
import com.ute.foodiedash.domain.promotion.enums.PromotionStatus;
import com.ute.foodiedash.domain.promotion.enums.PromotionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreatePromotionCommand(
    String code,
    String name,
    PromotionType type,
    EligibilityRule eligibilityRule,
    BigDecimal value,
    BigDecimal minOrderAmount,
    BigDecimal maxDiscountAmount,
    LocalDateTime startAt,
    LocalDateTime endAt,
    Integer totalUsageLimit,
    Integer perUserLimit,
    PromotionStatus status
) {}
