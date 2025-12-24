package com.ute.foodiedash.application.promotion.service;

import com.ute.foodiedash.application.promotion.query.PromotionDiscountQueryResult;
import com.ute.foodiedash.application.promotion.query.PromotionReservationQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.promotion.enums.DiscountAppliesTo;
import com.ute.foodiedash.domain.promotion.enums.PromotionUsageStatus;
import com.ute.foodiedash.domain.promotion.enums.PromotionType;
import com.ute.foodiedash.domain.promotion.model.Promotion;
import com.ute.foodiedash.domain.promotion.model.PromotionUsage;
import com.ute.foodiedash.domain.promotion.model.PromotionUsageCounter;
import com.ute.foodiedash.domain.promotion.repository.PromotionRepository;
import com.ute.foodiedash.domain.promotion.repository.PromotionRestaurantRepository;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageCounterRepository;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PromotionCheckoutService {
    private final PromotionRepository promotionRepository;
    private final PromotionRestaurantRepository promotionRestaurantRepository;
    private final PromotionUsageCounterRepository promotionUsageCounterRepository;
    private final PromotionUsageRepository promotionUsageRepository;

    @Transactional(readOnly = true)
    public PromotionCheckoutData prepareForCheckout(String promotionCode,
                                                    Long userId,
                                                    Long restaurantId,
                                                    BigDecimal orderSubtotal) {
        Promotion promotion = promotionRepository.findByCodeAndNotDeleted(promotionCode)
            .orElseThrow(() -> new BadRequestException("Promotion not active"));

        ensureEligibleOrThrow(promotion, userId, restaurantId, orderSubtotal);

        PromotionDiscountQueryResult discountRule = new PromotionDiscountQueryResult(
            promotion.getId(),
            promotion.getType(),
            promotion.getValue(),
            promotion.getMaxDiscountAmount(),
            DiscountAppliesTo.ORDER_TOTAL
        );

        BigDecimal discount = calculateDiscountFromRule(orderSubtotal, discountRule);
        return new PromotionCheckoutData(
            promotion.getId(),
            discountRule,
            discount
        );
    }

    @Transactional
    public PromotionReservationQueryResult reserve(Long promotionId, Long userId, Long orderId) {
        Promotion promotion = promotionRepository.findById(promotionId)
            .orElseThrow(() -> new NotFoundException("Promotion not found"));

        promotion.ensureUsable();

        PromotionUsage existing = promotionUsageRepository
            .findByPromotionIdAndOrderId(promotionId, orderId)
            .orElse(null);

        if (existing != null && existing.isNotReleased()) {
            throw new BadRequestException("Promotion already used for this order");
        }

        try {
            PromotionUsageCounter counter = promotionUsageCounterRepository.findByPromotionId(promotionId)
                .orElseGet(() -> promotionUsageCounterRepository.save(PromotionUsageCounter.createNew(promotionId)));

            if (promotion.isTotalUsageLimitExceeded(counter.getTotalUsed())) {
                throw new BadRequestException("Total usage limit exceeded");
            }

            if (promotion.getPerUserLimit() != null) {
                long userUsed = promotionUsageRepository.countByPromotionIdAndUserIdAndStatusIn(
                    promotionId,
                    userId,
                    List.of(PromotionUsageStatus.RESERVED, PromotionUsageStatus.USED)
                );
                if (promotion.isPerUserLimitExceeded(userUsed)) {
                    throw new BadRequestException("Per-user limit exceeded");
                }
            }

            PromotionUsage usage;
            if (existing != null) {
                existing.reReserve(userId, orderId);
                usage = existing;
            } else {
                usage = PromotionUsage.createReservation(promotionId, userId, orderId);
            }

            PromotionUsage saved = promotionUsageRepository.save(usage);
            counter.increment();
            promotionUsageCounterRepository.save(counter);

            return new PromotionReservationQueryResult(
                saved.getId(),
                saved.getPromotionId(),
                saved.getOrderId(),
                saved.getStatus()
            );
        } catch (OptimisticLockingFailureException e) {
            throw new BadRequestException("Promotion reservation conflict");
        }
    }

    public BigDecimal calculateDiscountFromRule(BigDecimal orderSubtotal,
                                                PromotionDiscountQueryResult discountRule) {
        if (orderSubtotal == null || discountRule == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount;
        if (discountRule.type() == PromotionType.FIXED_AMOUNT) {
            discount = discountRule.value();
        } else if (discountRule.type() == PromotionType.PERCENTAGE) {
            discount = orderSubtotal
                .multiply(discountRule.value())
                .divide(BigDecimal.valueOf(100));
        } else {
            throw new BadRequestException("Unsupported promotion type");
        }

        if (discountRule.maxDiscountAmount() != null
            && discount.compareTo(discountRule.maxDiscountAmount()) > 0) {
            discount = discountRule.maxDiscountAmount();
        }

        if (discount.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        if (discount.compareTo(orderSubtotal) > 0) {
            return orderSubtotal;
        }

        return discount;
    }

    private void ensureEligibleOrThrow(Promotion promotion,
                                       Long userId,
                                       Long restaurantId,
                                       BigDecimal orderSubtotal) {
        String failureReason = getEligibilityFailureReason(promotion, userId, restaurantId, orderSubtotal);
        if (failureReason != null) {
            throw new BadRequestException(failureReason);
        }
    }

    private String getEligibilityFailureReason(Promotion promotion,
                                               Long userId,
                                               Long restaurantId,
                                               BigDecimal orderSubtotal) {
        LocalDateTime now = LocalDateTime.now();
        if (!promotion.isActive()) {
            return "Promotion not active";
        }
        if (now.isBefore(promotion.getStartAt()) || now.isAfter(promotion.getEndAt())) {
            return "Promotion expired";
        }

        if (promotion.isRestaurantSpecific()) {
            boolean allowed = promotionRestaurantRepository
                .existsByPromotionIdAndRestaurantIdAndNotDeleted(promotion.getId(), restaurantId);
            if (!allowed) {
                return "Restaurant not eligible for this promotion";
            }
        }

        if (!promotion.isMinOrderMet(orderSubtotal)) {
            return "Minimum order amount not met";
        }

        if (promotion.getTotalUsageLimit() != null) {
            int totalUsed = promotionUsageCounterRepository.findByPromotionId(promotion.getId())
                .map(PromotionUsageCounter::getTotalUsed)
                .orElse(0);
            if (promotion.isTotalUsageLimitExceeded(totalUsed)) {
                return "Total usage limit exceeded";
            }
        }

        if (promotion.getPerUserLimit() != null) {
            long userUsed = promotionUsageRepository.countByPromotionIdAndUserIdAndStatus(
                promotion.getId(),
                userId,
                PromotionUsageStatus.USED
            );
            if (promotion.isPerUserLimitExceeded(userUsed)) {
                return "Per-user limit exceeded";
            }
        }

        return null;
    }

    public record PromotionCheckoutData(
        Long promotionId,
        PromotionDiscountQueryResult discountRule,
        BigDecimal discount
    ) {}
}
