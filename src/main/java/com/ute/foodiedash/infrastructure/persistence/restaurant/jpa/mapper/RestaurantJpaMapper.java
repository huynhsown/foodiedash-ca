package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity.RestaurantJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantJpaMapper {
    Restaurant toDomain(RestaurantJpaEntity jpaEntity);
    RestaurantJpaEntity toJpaEntity(Restaurant domain);
}
