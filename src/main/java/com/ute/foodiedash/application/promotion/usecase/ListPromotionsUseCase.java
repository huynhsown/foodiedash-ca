package com.ute.foodiedash.application.promotion.usecase;

import com.ute.foodiedash.application.promotion.port.PromotionQueryPort;
import com.ute.foodiedash.application.promotion.query.PromotionPageResult;
import com.ute.foodiedash.application.promotion.query.PromotionQueryResult;
import com.ute.foodiedash.domain.promotion.enums.EligibilityRule;
import com.ute.foodiedash.domain.promotion.enums.PromotionStatus;
import com.ute.foodiedash.domain.promotion.enums.PromotionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ListPromotionsUseCase {
    private final PromotionQueryPort promotionQueryPort;

    @Transactional(readOnly = true)
    public PromotionPageResult execute(
            String code, PromotionStatus status, PromotionType type,
            EligibilityRule eligibilityRule, String name,
            LocalDateTime startFrom, LocalDateTime startTo,
            LocalDateTime endFrom, LocalDateTime endTo,
            Boolean deleted, Pageable pageable) {

        Page<PromotionQueryResult> page = promotionQueryPort.search(
            code, status, type, eligibilityRule, name,
            startFrom, startTo, endFrom, endTo, deleted, pageable
        );

        return new PromotionPageResult(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isEmpty(),
            page.getNumberOfElements(),
            page.hasNext(),
            page.hasPrevious()
        );
    }
}
