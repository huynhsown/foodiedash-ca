package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.promotion.model.PromotionRestaurant;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.PromotionRestaurantJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionRestaurantJpaMapper {
    PromotionRestaurant toDomain(PromotionRestaurantJpaEntity jpaEntity);
    PromotionRestaurantJpaEntity toJpaEntity(PromotionRestaurant domain);
}
