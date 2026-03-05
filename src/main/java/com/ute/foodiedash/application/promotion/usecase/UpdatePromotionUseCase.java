package com.ute.foodiedash.application.promotion.usecase;

import com.ute.foodiedash.application.promotion.command.UpdatePromotionCommand;
import com.ute.foodiedash.application.promotion.query.PromotionQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.promotion.model.Promotion;
import com.ute.foodiedash.domain.promotion.model.PromotionUsageCounter;
import com.ute.foodiedash.domain.promotion.repository.PromotionRepository;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageCounterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdatePromotionUseCase {
    private final PromotionRepository promotionRepository;
    private final PromotionUsageCounterRepository promotionUsageCounterRepository;

    @Transactional
    public PromotionQueryResult execute(Long id, UpdatePromotionCommand command) {
        Promotion promotion = promotionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Promotion not found"));

        // Domain model handles validation internally
        promotion.update(
            command.name(), command.value(), command.minOrderAmount(),
            command.maxDiscountAmount(), command.startAt(), command.endAt(),
            command.totalUsageLimit(), command.perUserLimit()
        );

        // Handle totalUsageLimit via counter
        if (command.totalUsageLimit() != null) {
            PromotionUsageCounter counter = promotionUsageCounterRepository
                .findByPromotionId(id)
                .orElseThrow(() -> new NotFoundException("Promotion usage counter not found"));

            promotion.updateTotalUsageLimit(command.totalUsageLimit(), counter.getTotalUsed());
        }

        Promotion saved = promotionRepository.save(promotion);

        return toQueryResult(saved);
    }

    private PromotionQueryResult toQueryResult(Promotion p) {
        return new PromotionQueryResult(
            p.getId(), p.getCode(), p.getName(), p.getType(),
            p.getEligibilityRule(), p.getValue(), p.getMinOrderAmount(),
            p.getMaxDiscountAmount(), p.getStartAt(), p.getEndAt(),
            p.getTotalUsageLimit(), p.getPerUserLimit(), p.getStatus()
        );
    }
}
