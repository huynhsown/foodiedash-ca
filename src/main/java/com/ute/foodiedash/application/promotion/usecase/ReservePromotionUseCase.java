package com.ute.foodiedash.application.promotion.usecase;

import com.ute.foodiedash.application.promotion.command.ReservePromotionCommand;
import com.ute.foodiedash.application.promotion.query.PromotionReservationQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.promotion.enums.PromotionUsageStatus;
import com.ute.foodiedash.domain.promotion.model.Promotion;
import com.ute.foodiedash.domain.promotion.model.PromotionUsage;
import com.ute.foodiedash.domain.promotion.model.PromotionUsageCounter;
import com.ute.foodiedash.domain.promotion.repository.PromotionRepository;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageCounterRepository;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservePromotionUseCase {
    private final PromotionRepository promotionRepository;
    private final PromotionUsageRepository promotionUsageRepository;
    private final PromotionUsageCounterRepository promotionUsageCounterRepository;

    @Transactional
    public PromotionReservationQueryResult execute(ReservePromotionCommand command) {
        Promotion promotion = promotionRepository.findById(command.promotionId())
            .orElseThrow(() -> new NotFoundException("Promotion not found"));

        // Domain model validates usability
        promotion.ensureUsable();

        // Check existing usage for this order
        PromotionUsage existing = promotionUsageRepository
            .findByPromotionIdAndOrderId(command.promotionId(), command.orderId())
            .orElse(null);

        if (existing != null && existing.isNotReleased()) {
            throw new BadRequestException("PROMOTION_ALREADY_USED_FOR_ORDER");
        }

        try {
            PromotionUsageCounter counter = promotionUsageCounterRepository
                .findByPromotionId(command.promotionId())
                .orElseGet(() -> {
                    PromotionUsageCounter c = PromotionUsageCounter.createNew(command.promotionId());
                    return promotionUsageCounterRepository.save(c);
                });

            // Domain model checks limits
            if (promotion.isTotalUsageLimitExceeded(counter.getTotalUsed())) {
                throw new BadRequestException("TOTAL_USAGE_LIMIT_EXCEEDED");
            }

            if (promotion.getPerUserLimit() != null) {
                long userUsed = promotionUsageRepository
                    .countByPromotionIdAndUserIdAndStatusIn(
                        command.promotionId(), command.userId(),
                        List.of(PromotionUsageStatus.RESERVED, PromotionUsageStatus.USED)
                    );
                if (promotion.isPerUserLimitExceeded(userUsed)) {
                    throw new BadRequestException("PER_USER_LIMIT_EXCEEDED");
                }
            }

            // Create or re-reserve usage
            PromotionUsage usage;
            if (existing != null) {
                existing.reReserve(command.userId(), command.orderId());
                usage = existing;
            } else {
                usage = PromotionUsage.createReservation(
                    command.promotionId(), command.userId(), command.orderId()
                );
            }

            PromotionUsage savedUsage = promotionUsageRepository.save(usage);

            // Domain model increments counter
            counter.increment();
            promotionUsageCounterRepository.save(counter);

            return new PromotionReservationQueryResult(
                savedUsage.getId(), savedUsage.getPromotionId(),
                savedUsage.getOrderId(), savedUsage.getStatus()
            );
        } catch (OptimisticLockingFailureException e) {
            throw new BadRequestException("PROMOTION_RESERVATION_CONFLICT");
        }
    }
}
