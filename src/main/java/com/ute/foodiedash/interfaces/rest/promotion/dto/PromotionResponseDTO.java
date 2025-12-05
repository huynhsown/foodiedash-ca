package com.ute.foodiedash.interfaces.rest.promotion.dto;

import com.ute.foodiedash.domain.promotion.enums.EligibilityRule;
import com.ute.foodiedash.domain.promotion.enums.PromotionStatus;
import com.ute.foodiedash.domain.promotion.enums.PromotionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PromotionResponseDTO {
    private Long id;
    private String code;
    private String name;
    private PromotionType type;
    private EligibilityRule eligibilityRule;
    private BigDecimal value;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer totalUsageLimit;
    private Integer perUserLimit;
    private PromotionStatus status;
}
