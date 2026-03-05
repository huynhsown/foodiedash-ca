package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.RestaurantImage;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantImageJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantImageJpaMapper {
    RestaurantImage toDomain(RestaurantImageJpaEntity jpaEntity);
    RestaurantImageJpaEntity toJpaEntity(RestaurantImage domain);
}
