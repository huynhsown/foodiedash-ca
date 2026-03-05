package com.ute.foodiedash.domain.promotion.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionUsageCounter extends BaseEntity {
    private Long promotionId;
    private Integer totalUsed;

    public static PromotionUsageCounter createNew(Long promotionId) {
        PromotionUsageCounter counter = new PromotionUsageCounter();
        counter.promotionId = promotionId;
        counter.totalUsed = 0;
        return counter;
    }

    public void increment() {
        this.totalUsed = (this.totalUsed != null ? this.totalUsed : 0) + 1;
    }

    public void decrement() {
        if (this.totalUsed != null && this.totalUsed > 0) {
            this.totalUsed = this.totalUsed - 1;
        }
    }
}
