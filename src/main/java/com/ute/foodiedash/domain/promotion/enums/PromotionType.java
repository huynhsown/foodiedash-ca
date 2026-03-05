package com.ute.foodiedash.domain.promotion.enums;

import com.ute.foodiedash.domain.common.exception.BadRequestException;

import java.math.BigDecimal;

public enum PromotionType {

    FIXED_AMOUNT {
        @Override
        public void validate(BigDecimal value, BigDecimal minOrderAmount, Integer perUserLimit) {
            if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("Fixed amount value must be greater than 0");
            }
        }
    },

    PERCENTAGE {
        @Override
        public void validate(BigDecimal value, BigDecimal minOrderAmount, Integer perUserLimit) {
            if (value == null
                    || value.compareTo(BigDecimal.ZERO) <= 0
                    || value.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new BadRequestException("Percentage value must be between 0 and 100");
            }
        }
    };

    public abstract void validate(BigDecimal value, BigDecimal minOrderAmount, Integer perUserLimit);
}
