package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.RestaurantPause;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantPauseJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantPauseJpaMapper {
    RestaurantPause toDomain(RestaurantPauseJpaEntity jpaEntity);
    RestaurantPauseJpaEntity toJpaEntity(RestaurantPause domain);
}
