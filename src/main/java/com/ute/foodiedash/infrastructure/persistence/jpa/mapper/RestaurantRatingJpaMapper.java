package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.RestaurantRating;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.RestaurantRatingJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantRatingJpaMapper {
    RestaurantRating toDomain(RestaurantRatingJpaEntity jpaEntity);
    RestaurantRatingJpaEntity toJpaEntity(RestaurantRating domain);
}
