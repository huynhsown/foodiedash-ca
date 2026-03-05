package com.ute.foodiedash.domain.promotion.repository;

import com.ute.foodiedash.domain.promotion.enums.PromotionUsageStatus;
import com.ute.foodiedash.domain.promotion.model.PromotionUsage;

import java.util.Collection;
import java.util.Optional;

public interface PromotionUsageRepository {
    PromotionUsage save(PromotionUsage usage);
    Optional<PromotionUsage> findByPromotionIdAndOrderId(Long promotionId, Long orderId);
    long countByPromotionIdAndUserIdAndStatus(Long promotionId, Long userId, PromotionUsageStatus status);
    long countByPromotionIdAndUserIdAndStatusIn(Long promotionId, Long userId, Collection<PromotionUsageStatus> statuses);
}
