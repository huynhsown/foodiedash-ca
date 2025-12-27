package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.RestaurantPause;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity.RestaurantPauseJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantPauseJpaMapper {
    RestaurantPause toDomain(RestaurantPauseJpaEntity jpaEntity);
    RestaurantPauseJpaEntity toJpaEntity(RestaurantPause domain);
}
