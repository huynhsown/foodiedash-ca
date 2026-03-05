package com.ute.foodiedash.application.promotion.usecase;

import com.ute.foodiedash.application.promotion.query.PromotionDiscountQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.promotion.enums.DiscountAppliesTo;
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

@Component
@RequiredArgsConstructor
public class GetDiscountRuleUseCase {
    private final PromotionRepository promotionRepository;
    private final PromotionRestaurantRepository promotionRestaurantRepository;
    private final PromotionUsageCounterRepository promotionUsageCounterRepository;
    private final PromotionUsageRepository promotionUsageRepository;

    @Transactional(readOnly = true)
    public PromotionDiscountQueryResult execute(String promotionCode, Long userId,
                                                 Long restaurantId, BigDecimal orderSubtotal) {
        Promotion promotion = promotionRepository.findByCodeAndNotDeleted(promotionCode)
            .orElse(null);

        if (promotion == null || !promotion.isActive()) {
            throw new BadRequestException("PROMOTION_NOT_ACTIVE");
        }

        // Domain model validates usability
        promotion.ensureUsable();

        if (promotion.isRestaurantSpecific()) {
            boolean allowed = promotionRestaurantRepository
                .existsByPromotionIdAndRestaurantIdAndNotDeleted(promotion.getId(), restaurantId);
            if (!allowed) {
                throw new BadRequestException("RESTAURANT_NOT_ELIGIBLE");
            }
        }

        promotion.ensureMinOrderMet(orderSubtotal);

        if (promotion.getTotalUsageLimit() != null) {
            int totalUsed = promotionUsageCounterRepository
                .findByPromotionId(promotion.getId())
                .map(PromotionUsageCounter::getTotalUsed)
                .orElse(0);
            if (promotion.isTotalUsageLimitExceeded(totalUsed)) {
                throw new BadRequestException("TOTAL_USAGE_LIMIT_EXCEEDED");
            }
        }

        if (promotion.getPerUserLimit() != null) {
            long userUsed = promotionUsageRepository
                .countByPromotionIdAndUserIdAndStatus(
                    promotion.getId(), userId, PromotionUsageStatus.USED
                );
            if (promotion.isPerUserLimitExceeded(userUsed)) {
                throw new BadRequestException("PER_USER_LIMIT_EXCEEDED");
            }
        }

        return new PromotionDiscountQueryResult(
            promotion.getId(), promotion.getType(), promotion.getValue(),
            promotion.getMaxDiscountAmount(), DiscountAppliesTo.ORDER_TOTAL
        );
    }
}
