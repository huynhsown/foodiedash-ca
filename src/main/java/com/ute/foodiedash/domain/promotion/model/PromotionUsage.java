package com.ute.foodiedash.domain.promotion.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.promotion.enums.PromotionUsageStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionUsage extends BaseEntity {
    private Long id;
    private Long promotionId;
    private Long userId;
    private Long orderId;
    private PromotionUsageStatus status;

    public static PromotionUsage createReservation(Long promotionId, Long userId, Long orderId) {
        PromotionUsage usage = new PromotionUsage();
        usage.promotionId = promotionId;
        usage.userId = userId;
        usage.orderId = orderId;
        usage.status = PromotionUsageStatus.RESERVED;
        return usage;
    }

    public void confirm() {
        if (this.status != PromotionUsageStatus.RESERVED) {
            throw new BadRequestException("PROMOTION_USAGE_NOT_RESERVED");
        }
        this.status = PromotionUsageStatus.USED;
    }

    public void release() {
        if (this.status != PromotionUsageStatus.RESERVED) {
            throw new BadRequestException("PROMOTION_USAGE_NOT_RESERVED");
        }
        this.status = PromotionUsageStatus.RELEASED;
    }

    public boolean isReleased() {
        return this.status == PromotionUsageStatus.RELEASED;
    }

    public boolean isNotReleased() {
        return this.status != PromotionUsageStatus.RELEASED;
    }

    // ========== Re-reserve a released usage ==========
    public void reReserve(Long userId, Long orderId) {
        this.userId = userId;
        this.orderId = orderId;
        this.status = PromotionUsageStatus.RESERVED;
    }

}
