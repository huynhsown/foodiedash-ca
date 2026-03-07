package com.ute.foodiedash.infrastructure.persistence.promotion.jpa.repository;

import com.ute.foodiedash.domain.promotion.enums.PromotionUsageStatus;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity.PromotionUsageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PromotionUsageJpaRepository extends JpaRepository<PromotionUsageJpaEntity, Long> {

    Optional<PromotionUsageJpaEntity> findByPromotionIdAndOrderId(Long promotionId, Long orderId);

    long countByPromotionIdAndUserIdAndStatus(Long promotionId, Long userId, PromotionUsageStatus status);

    long countByPromotionIdAndUserIdAndStatusIn(Long promotionId, Long userId, Collection<PromotionUsageStatus> statuses);
}
