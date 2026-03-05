package com.ute.foodiedash.infrastructure.persistence.adapter;

import com.ute.foodiedash.domain.promotion.enums.PromotionUsageStatus;
import com.ute.foodiedash.domain.promotion.model.PromotionUsage;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageRepository;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.PromotionUsageJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.jpa.mapper.PromotionUsageJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.jpa.repository.PromotionUsageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PromotionUsageRepositoryAdapter implements PromotionUsageRepository {
    private final PromotionUsageJpaRepository jpaRepository;
    private final PromotionUsageJpaMapper mapper;

    @Override
    public PromotionUsage save(PromotionUsage usage) {
        PromotionUsageJpaEntity jpaEntity = mapper.toJpaEntity(usage);
        PromotionUsageJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<PromotionUsage> findByPromotionIdAndOrderId(Long promotionId, Long orderId) {
        return jpaRepository.findByPromotionIdAndOrderId(promotionId, orderId)
            .map(mapper::toDomain);
    }

    @Override
    public long countByPromotionIdAndUserIdAndStatus(Long promotionId, Long userId, PromotionUsageStatus status) {
        return jpaRepository.countByPromotionIdAndUserIdAndStatus(promotionId, userId, status);
    }

    @Override
    public long countByPromotionIdAndUserIdAndStatusIn(Long promotionId, Long userId, Collection<PromotionUsageStatus> statuses) {
        return jpaRepository.countByPromotionIdAndUserIdAndStatusIn(promotionId, userId, statuses);
    }
}
