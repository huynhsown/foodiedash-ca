package com.ute.foodiedash.application.promotion.usecase;

import com.ute.foodiedash.application.promotion.command.CreatePromotionCommand;
import com.ute.foodiedash.application.promotion.query.PromotionQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.promotion.model.Promotion;
import com.ute.foodiedash.domain.promotion.model.PromotionUsageCounter;
import com.ute.foodiedash.domain.promotion.repository.PromotionRepository;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageCounterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreatePromotionUseCase {
    private final PromotionRepository promotionRepository;
    private final PromotionUsageCounterRepository promotionUsageCounterRepository;

    @Transactional
    public PromotionQueryResult execute(CreatePromotionCommand command) {
        if (promotionRepository.existsByCode(command.code())) {
            throw new BadRequestException("Promotion code already exists");
        }

        Promotion promotion = Promotion.create(
            command.code(), command.name(), command.type(),
            command.eligibilityRule(), command.value(),
            command.minOrderAmount(), command.maxDiscountAmount(),
            command.startAt(), command.endAt(),
            command.totalUsageLimit(), command.perUserLimit(),
            command.status()
        );

        Promotion saved = promotionRepository.save(promotion);

        // Initialize usage counter for this promotion
        PromotionUsageCounter counter = PromotionUsageCounter.createNew(saved.getId());
        promotionUsageCounterRepository.save(counter);

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
