package com.ute.foodiedash.infrastructure.persistence.promotion.adapter;

import com.ute.foodiedash.application.promotion.port.PromotionQueryPort;
import com.ute.foodiedash.application.promotion.query.PromotionQueryResult;
import com.ute.foodiedash.domain.promotion.enums.EligibilityRule;
import com.ute.foodiedash.domain.promotion.enums.PromotionStatus;
import com.ute.foodiedash.domain.promotion.enums.PromotionType;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity.PromotionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.repository.PromotionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PromotionQueryAdapter implements PromotionQueryPort {
    private final PromotionJpaRepository jpaRepository;

    @Override
    public Page<PromotionQueryResult> search(
            String code, PromotionStatus status, PromotionType type,
            EligibilityRule eligibilityRule, String name,
            LocalDateTime startFrom, LocalDateTime startTo,
            LocalDateTime endFrom, LocalDateTime endTo,
            Boolean deleted, Pageable pageable) {

        Page<PromotionJpaEntity> page = jpaRepository.search(
            code, status, type, eligibilityRule, name,
            startFrom, startTo, endFrom, endTo, deleted, pageable
        );

        return page.map(this::toQueryResult);
    }

    private PromotionQueryResult toQueryResult(PromotionJpaEntity entity) {
        return new PromotionQueryResult(
            entity.getId(), entity.getCode(), entity.getName(), entity.getType(),
            entity.getEligibilityRule(), entity.getValue(), entity.getMinOrderAmount(),
            entity.getMaxDiscountAmount(), entity.getStartAt(), entity.getEndAt(),
            entity.getTotalUsageLimit(), entity.getPerUserLimit(), entity.getStatus()
        );
    }
}
