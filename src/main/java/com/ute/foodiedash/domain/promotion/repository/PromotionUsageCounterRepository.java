package com.ute.foodiedash.domain.promotion.repository;

import com.ute.foodiedash.domain.promotion.model.PromotionUsageCounter;

import java.util.Optional;

public interface PromotionUsageCounterRepository {
    PromotionUsageCounter save(PromotionUsageCounter counter);
    Optional<PromotionUsageCounter> findByPromotionId(Long promotionId);
}
