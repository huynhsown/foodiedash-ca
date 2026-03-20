package com.ute.foodiedash.infrastructure.persistence.promotion.adapter;

import com.ute.foodiedash.domain.promotion.model.PromotionRestaurant;
import com.ute.foodiedash.domain.promotion.repository.PromotionRestaurantRepository;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity.PromotionRestaurantJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.mapper.PromotionRestaurantJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.repository.PromotionRestaurantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionRestaurantRepositoryAdapter implements PromotionRestaurantRepository {
    private final PromotionRestaurantJpaRepository jpaRepository;
    private final PromotionRestaurantJpaMapper mapper;

    @Override
    public PromotionRestaurant save(PromotionRestaurant promotionRestaurant) {
        PromotionRestaurantJpaEntity jpaEntity;
        if (promotionRestaurant.getId() == null) {
            jpaEntity = mapper.toJpaEntity(promotionRestaurant);
        } else {
            jpaEntity = jpaRepository.findById(promotionRestaurant.getId())
                    .orElseThrow();
            mapper.updateEntity(jpaEntity, promotionRestaurant);
        }
        PromotionRestaurantJpaEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public boolean existsByPromotionIdAndRestaurantIdAndNotDeleted(Long promotionId, Long restaurantId) {
        return jpaRepository.existsByPromotionIdAndRestaurantIdAndDeletedAtIsNull(promotionId, restaurantId);
    }
}
