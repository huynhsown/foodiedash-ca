package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.RestaurantRating;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity.RestaurantRatingJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RestaurantRatingJpaMapper {
    RestaurantRating toDomain(RestaurantRatingJpaEntity jpaEntity);
    RestaurantRatingJpaEntity toJpaEntity(RestaurantRating domain);

    void updateEntity(@MappingTarget RestaurantRatingJpaEntity entity, RestaurantRating domain);
}
