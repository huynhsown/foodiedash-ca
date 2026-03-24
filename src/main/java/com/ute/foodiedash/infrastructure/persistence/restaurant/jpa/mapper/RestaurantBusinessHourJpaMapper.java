package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.RestaurantBusinessHour;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity.RestaurantBusinessHourJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RestaurantBusinessHourJpaMapper {
    RestaurantBusinessHour toDomain(RestaurantBusinessHourJpaEntity jpaEntity);
    RestaurantBusinessHourJpaEntity toJpaEntity(RestaurantBusinessHour domain);

    void updateEntity(@MappingTarget RestaurantBusinessHourJpaEntity entity, RestaurantBusinessHour domain);
}
