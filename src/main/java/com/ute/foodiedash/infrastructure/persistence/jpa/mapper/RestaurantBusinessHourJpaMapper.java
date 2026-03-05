package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.RestaurantBusinessHour;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantBusinessHourJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantBusinessHourJpaMapper {
    RestaurantBusinessHour toDomain(RestaurantBusinessHourJpaEntity jpaEntity);
    RestaurantBusinessHourJpaEntity toJpaEntity(RestaurantBusinessHour domain);
}
