package com.ute.foodiedash.infrastructure.persistence.promotion.adapter;

import com.ute.foodiedash.domain.promotion.model.PromotionUsageCounter;
import com.ute.foodiedash.domain.promotion.repository.PromotionUsageCounterRepository;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity.PromotionUsageCounterJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.mapper.PromotionUsageCounterJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.repository.PromotionUsageCounterJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PromotionUsageCounterRepositoryAdapter implements PromotionUsageCounterRepository {
    private final PromotionUsageCounterJpaRepository jpaRepository;
    private final PromotionUsageCounterJpaMapper mapper;

    @Override
    public PromotionUsageCounter save(PromotionUsageCounter counter) {
        PromotionUsageCounterJpaEntity jpaEntity = mapper.toJpaEntity(counter);
        PromotionUsageCounterJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<PromotionUsageCounter> findByPromotionId(Long promotionId) {
        return jpaRepository.findByPromotionId(promotionId)
            .map(mapper::toDomain);
    }
}
