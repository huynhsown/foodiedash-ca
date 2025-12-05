package com.ute.foodiedash.interfaces.rest.promotion.dto;

import com.ute.foodiedash.domain.promotion.enums.EligibilityRule;
import com.ute.foodiedash.domain.promotion.enums.PromotionStatus;
import com.ute.foodiedash.domain.promotion.enums.PromotionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreatePromotionRequestDTO {
    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @NotNull
    private PromotionType type;

    @NotNull
    private EligibilityRule eligibilityRule;

    @Positive
    private BigDecimal value;

    @PositiveOrZero
    private BigDecimal minOrderAmount;

    @Positive
    private BigDecimal maxDiscountAmount;

    @NotNull
    private LocalDateTime startAt;

    @NotNull
    private LocalDateTime endAt;

    @Positive
    private Integer totalUsageLimit;

    @Positive
    private Integer perUserLimit;

    @NotNull
    private PromotionStatus status;
}
