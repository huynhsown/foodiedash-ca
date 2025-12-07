package com.ute.foodiedash.infrastructure.persistence.promotion.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity.PromotionUsageCounterJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionUsageCounterJpaRepository extends JpaRepository<PromotionUsageCounterJpaEntity, Long> {
    Optional<PromotionUsageCounterJpaEntity> findByPromotionId(Long promotionId);
}
