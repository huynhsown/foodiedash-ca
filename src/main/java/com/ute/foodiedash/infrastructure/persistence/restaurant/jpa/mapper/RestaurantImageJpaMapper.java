package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.RestaurantImage;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity.RestaurantImageJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RestaurantImageJpaMapper {
    RestaurantImage toDomain(RestaurantImageJpaEntity jpaEntity);
    RestaurantImageJpaEntity toJpaEntity(RestaurantImage domain);

    void updateEntity(@MappingTarget RestaurantImageJpaEntity entity, RestaurantImage domain);
}
