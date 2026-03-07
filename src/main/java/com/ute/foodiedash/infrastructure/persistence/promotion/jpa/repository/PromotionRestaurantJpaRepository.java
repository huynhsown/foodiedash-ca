package com.ute.foodiedash.infrastructure.persistence.promotion.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity.PromotionRestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRestaurantJpaRepository extends JpaRepository<PromotionRestaurantJpaEntity, Long> {
    boolean existsByPromotionIdAndRestaurantIdAndDeletedAtIsNull(Long promotionId, Long restaurantId);
}
