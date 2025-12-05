package com.ute.foodiedash.application.promotion.usecase;

import com.ute.foodiedash.application.promotion.query.PromotionEligibilityQueryResult;
import com.ute.foodiedash.domain.promotion.enums.PromotionUsageStatus;
import com.ute.foodiedash.domain.promotion.model.Promotion;
import com.ute.foodiedash.domain.promotion.model.PromotionUsageCounter;
import com.ute.foodiedash.domain.promotion.repository.PromotionRepository;
import com.ute.foodiedash.domain.promotion.repository.PromotionRestaurantRepository;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageCounterRepository;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ValidatePromotionEligibilityUseCase {
    private final PromotionRepository promotionRepository;
    private final PromotionRestaurantRepository promotionRestaurantRepository;
    private final PromotionUsageCounterRepository promotionUsageCounterRepository;
    private final PromotionUsageRepository promotionUsageRepository;

    @Transactional(readOnly = true)
    public PromotionEligibilityQueryResult execute(String promotionCode, Long userId,
                                                    Long restaurantId, BigDecimal orderSubtotal) {
        Promotion promotion = promotionRepository.findByCode(promotionCode).orElse(null);

        if (promotion == null || !promotion.isActive()) {
            return PromotionEligibilityQueryResult.fail("PROMOTION_NOT_ACTIVE");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(promotion.getStartAt()) || now.isAfter(promotion.getEndAt())) {
            return PromotionEligibilityQueryResult.fail("PROMOTION_EXPIRED");
        }

        if (promotion.isRestaurantSpecific()) {
            boolean allowed = promotionRestaurantRepository
                .existsByPromotionIdAndRestaurantIdAndNotDeleted(promotion.getId(), restaurantId);
            if (!allowed) {
                return PromotionEligibilityQueryResult.fail("RESTAURANT_NOT_ELIGIBLE");
            }
        }

        if (!promotion.isMinOrderMet(orderSubtotal)) {
            return PromotionEligibilityQueryResult.fail("MIN_ORDER_NOT_MET");
        }

        if (promotion.getTotalUsageLimit() != null) {
            int totalUsed = promotionUsageCounterRepository
                .findByPromotionId(promotion.getId())
                .map(PromotionUsageCounter::getTotalUsed)
                .orElse(0);
            if (promotion.isTotalUsageLimitExceeded(totalUsed)) {
                return PromotionEligibilityQueryResult.fail("TOTAL_USAGE_LIMIT_EXCEEDED");
            }
        }

        if (promotion.getPerUserLimit() != null) {
            long userUsed = promotionUsageRepository
                .countByPromotionIdAndUserIdAndStatus(
                    promotion.getId(), userId, PromotionUsageStatus.USED
                );
            if (promotion.isPerUserLimitExceeded(userUsed)) {
                return PromotionEligibilityQueryResult.fail("PER_USER_LIMIT_EXCEEDED");
            }
        }

        return PromotionEligibilityQueryResult.ok(promotion.getId());
    }
}
