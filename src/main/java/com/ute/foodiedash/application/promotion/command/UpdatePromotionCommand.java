package com.ute.foodiedash.application.promotion.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdatePromotionCommand(
    String name,
    BigDecimal value,
    BigDecimal minOrderAmount,
    BigDecimal maxDiscountAmount,
    LocalDateTime startAt,
    LocalDateTime endAt,
    Integer totalUsageLimit,
    Integer perUserLimit
) {}
