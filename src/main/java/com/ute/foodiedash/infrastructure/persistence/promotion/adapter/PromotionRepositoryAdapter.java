package com.ute.foodiedash.infrastructure.persistence.promotion.adapter;

import com.ute.foodiedash.domain.promotion.model.Promotion;
import com.ute.foodiedash.domain.promotion.repository.PromotionRepository;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity.PromotionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.mapper.PromotionJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.repository.PromotionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PromotionRepositoryAdapter implements PromotionRepository {
    private final PromotionJpaRepository jpaRepository;
    private final PromotionJpaMapper mapper;

    @Override
    public Promotion save(Promotion promotion) {
        PromotionJpaEntity jpaEntity = mapper.toJpaEntity(promotion);
        PromotionJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Promotion> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Promotion> findByCode(String code) {
        return jpaRepository.findByCode(code).map(mapper::toDomain);
    }

    @Override
    public Optional<Promotion> findByCodeAndNotDeleted(String code) {
        return jpaRepository.findByCodeAndDeletedAtIsNull(code).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    @Override
    public void softDeleteById(Long id) {
        jpaRepository.softDeleteById(id);
    }

    @Override
    public void restoreById(Long id) {
        jpaRepository.restoreById(id);
    }
}
