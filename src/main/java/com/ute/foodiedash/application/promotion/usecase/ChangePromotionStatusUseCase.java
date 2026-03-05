package com.ute.foodiedash.application.promotion.usecase;

import com.ute.foodiedash.application.promotion.command.ChangePromotionStatusCommand;
import com.ute.foodiedash.application.promotion.query.PromotionQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.promotion.model.Promotion;
import com.ute.foodiedash.domain.promotion.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChangePromotionStatusUseCase {
    private final PromotionRepository promotionRepository;

    @Transactional
    public PromotionQueryResult execute(Long id, ChangePromotionStatusCommand command) {
        Promotion promotion = promotionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Promotion not found"));

        promotion.changeStatus(command.status());

        Promotion saved = promotionRepository.save(promotion);

        return new PromotionQueryResult(
            saved.getId(), saved.getCode(), saved.getName(), saved.getType(),
            saved.getEligibilityRule(), saved.getValue(), saved.getMinOrderAmount(),
            saved.getMaxDiscountAmount(), saved.getStartAt(), saved.getEndAt(),
            saved.getTotalUsageLimit(), saved.getPerUserLimit(), saved.getStatus()
        );
    }
}
