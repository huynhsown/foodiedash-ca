package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.RestaurantCategory;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity.RestaurantCategoryJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantCategoryJpaMapper {
    RestaurantCategory toDomain(RestaurantCategoryJpaEntity jpaEntity);
    RestaurantCategoryJpaEntity toJpaEntity(RestaurantCategory domain);
}
