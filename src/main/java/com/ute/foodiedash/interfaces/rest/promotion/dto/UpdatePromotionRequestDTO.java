package com.ute.foodiedash.interfaces.rest.promotion.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UpdatePromotionRequestDTO {
    private String name;

    @Positive
    private BigDecimal value;

    @PositiveOrZero
    private BigDecimal minOrderAmount;

    @Positive
    private BigDecimal maxDiscountAmount;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @Positive
    private Integer totalUsageLimit;

    @Positive
    private Integer perUserLimit;
}
