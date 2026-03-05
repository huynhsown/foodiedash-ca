package com.ute.foodiedash.application.promotion.usecase;

import com.ute.foodiedash.application.promotion.command.ConfirmPromotionUsageCommand;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.promotion.model.PromotionUsage;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ConfirmPromotionUsageUseCase {
    private final PromotionUsageRepository promotionUsageRepository;

    @Transactional
    public void execute(ConfirmPromotionUsageCommand command) {
        PromotionUsage usage = promotionUsageRepository
            .findByPromotionIdAndOrderId(command.promotionId(), command.orderId())
            .orElseThrow(() -> new NotFoundException("Promotion usage not found"));

        usage.confirm();

        promotionUsageRepository.save(usage);
    }
}
