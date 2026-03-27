package com.ute.foodiedash.domain.promotion.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.promotion.enums.EligibilityRule;
import com.ute.foodiedash.domain.promotion.enums.PromotionStatus;
import com.ute.foodiedash.domain.promotion.enums.PromotionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Promotion extends BaseEntity {
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

    public static Promotion create(String code, String name, PromotionType type,
                                   EligibilityRule eligibilityRule, BigDecimal value,
                                   BigDecimal minOrderAmount, BigDecimal maxDiscountAmount,
                                   LocalDateTime startAt, LocalDateTime endAt,
                                   Integer totalUsageLimit, Integer perUserLimit,
                                   PromotionStatus status) {
        Promotion p = new Promotion();
        p.code = code;
        p.name = name;
        p.type = type;
        p.eligibilityRule = eligibilityRule != null ? eligibilityRule : EligibilityRule.NONE;
        p.value = value;
        p.minOrderAmount = minOrderAmount != null ? minOrderAmount : BigDecimal.ZERO;
        p.maxDiscountAmount = maxDiscountAmount;
        p.startAt = startAt;
        p.endAt = endAt;
        p.totalUsageLimit = totalUsageLimit;
        p.perUserLimit = perUserLimit;
        p.status = status;

        p.type.validate(value, minOrderAmount, perUserLimit);
        return p;
    }

    public void activate() {
        if (this.status == PromotionStatus.ACTIVE) {
            throw new BadRequestException("Promotion already has this status");
        }
        if (this.endAt != null && this.endAt.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Cannot activate an expired promotion");
        }
        this.status = PromotionStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status == PromotionStatus.INACTIVE) {
            throw new BadRequestException("Promotion already has this status");
        }
        this.status = PromotionStatus.INACTIVE;
    }

    public void changeStatus(PromotionStatus newStatus) {
        if (newStatus == PromotionStatus.ACTIVE) {
            activate();
        } else {
            deactivate();
        }
    }

    public void update(String name, BigDecimal value, BigDecimal minOrderAmount,
                       BigDecimal maxDiscountAmount, LocalDateTime startAt,
                       LocalDateTime endAt, Integer totalUsageLimit,
                       Integer perUserLimit) {
        boolean isActive = this.status == PromotionStatus.ACTIVE;

        if (isActive) {
            if (value != null || minOrderAmount != null || maxDiscountAmount != null) {
                throw new BadRequestException("Cannot modify discount of an active promotion");
            }
            if (startAt != null) {
                throw new BadRequestException("Cannot modify start time of an active promotion");
            }
            if (endAt != null && endAt.isBefore(this.endAt)) {
                throw new BadRequestException("Cannot shorten an active promotion");
            }
        }

        LocalDateTime effectiveStartAt = startAt != null ? startAt : this.startAt;
        if (endAt != null && endAt.isBefore(effectiveStartAt)) {
            throw new BadRequestException("End time must be after start time");
        }

        if (perUserLimit != null && this.perUserLimit != null
                && perUserLimit < this.perUserLimit) {
            throw new BadRequestException("Per-user limit cannot be reduced");
        }

        if (name != null) this.name = name;
        if (value != null) this.value = value;
        if (minOrderAmount != null) this.minOrderAmount = minOrderAmount;
        if (maxDiscountAmount != null) this.maxDiscountAmount = maxDiscountAmount;
        if (startAt != null) this.startAt = startAt;
        if (endAt != null) this.endAt = endAt;
        if (perUserLimit != null) this.perUserLimit = perUserLimit;
    }

    public void updateTotalUsageLimit(Integer newLimit, int currentTotalUsed) {
        if (newLimit < currentTotalUsed) {
            throw new BadRequestException("Total usage limit cannot be less than current usage");
        }
        if (this.status == PromotionStatus.ACTIVE
                && this.totalUsageLimit != null
                && newLimit < this.totalUsageLimit) {
            throw new BadRequestException("Cannot reduce total usage limit of an active promotion");
        }
        this.totalUsageLimit = newLimit;
    }

    public void ensureUsable() {
        if (this.status != PromotionStatus.ACTIVE) {
            throw new BadRequestException("Promotion not active");
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(this.startAt) || now.isAfter(this.endAt)) {
            throw new BadRequestException("Promotion expired");
        }
    }

    public boolean isActive() {
        return this.status == PromotionStatus.ACTIVE;
    }

    public boolean isRestaurantSpecific() {
        return EligibilityRule.RESTAURANT_SPECIFIC.equals(this.eligibilityRule);
    }

    public boolean isMinOrderMet(BigDecimal orderSubtotal) {
        return orderSubtotal.compareTo(this.minOrderAmount) >= 0;
    }

    public void ensureMinOrderMet(BigDecimal orderSubtotal) {
        if (!isMinOrderMet(orderSubtotal)) {
            throw new BadRequestException("MIN_ORDER_NOT_MET");
        }
    }

    public boolean isTotalUsageLimitExceeded(int totalUsed) {
        return this.totalUsageLimit != null && totalUsed >= this.totalUsageLimit;
    }

    public boolean isPerUserLimitExceeded(long userUsedCount) {
        return this.perUserLimit != null && userUsedCount >= this.perUserLimit;
    }
}
