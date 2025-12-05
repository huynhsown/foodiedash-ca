package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantJpaMapper {
    Restaurant toDomain(RestaurantJpaEntity jpaEntity);
    RestaurantJpaEntity toJpaEntity(Restaurant domain);
}
