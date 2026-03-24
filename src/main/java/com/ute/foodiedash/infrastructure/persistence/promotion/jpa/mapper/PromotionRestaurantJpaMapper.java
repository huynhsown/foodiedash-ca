package com.ute.foodiedash.infrastructure.persistence.promotion.jpa.mapper;

import com.ute.foodiedash.domain.promotion.model.PromotionRestaurant;
import com.ute.foodiedash.infrastructure.persistence.promotion.jpa.entity.PromotionRestaurantJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PromotionRestaurantJpaMapper {
    PromotionRestaurant toDomain(PromotionRestaurantJpaEntity jpaEntity);
    PromotionRestaurantJpaEntity toJpaEntity(PromotionRestaurant domain);

    void updateEntity(@MappingTarget PromotionRestaurantJpaEntity entity, PromotionRestaurant domain);
}
