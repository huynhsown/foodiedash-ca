package com.ute.foodiedash.application.promotion.usecase;

import com.ute.foodiedash.application.promotion.command.ReleasePromotionCommand;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.promotion.model.PromotionUsage;
import com.ute.foodiedash.domain.promotion.model.PromotionUsageCounter;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageCounterRepository;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReleasePromotionUseCase {
    private final PromotionUsageRepository promotionUsageRepository;
    private final PromotionUsageCounterRepository promotionUsageCounterRepository;

    @Transactional
    public void execute(ReleasePromotionCommand command) {
        PromotionUsage usage = promotionUsageRepository
            .findByPromotionIdAndOrderId(command.promotionId(), command.orderId())
            .orElseThrow(() -> new NotFoundException("Promotion usage not found"));

        // Domain model handles state transition validation
        usage.release();
        promotionUsageRepository.save(usage);

        try {
            PromotionUsageCounter counter = promotionUsageCounterRepository
                .findByPromotionId(command.promotionId())
                .orElseThrow(() -> new NotFoundException("Promotion usage counter not found"));

            // Domain model decrements counter safely
            counter.decrement();
            promotionUsageCounterRepository.save(counter);
        } catch (OptimisticLockingFailureException e) {
            throw new BadRequestException("PROMOTION_RELEASE_CONFLICT");
        }
    }
}
