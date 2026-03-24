package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.RestaurantCategoryMap;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity.RestaurantCategoryMapJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RestaurantCategoryMapJpaMapper {
    RestaurantCategoryMap toDomain(RestaurantCategoryMapJpaEntity jpaEntity);
    RestaurantCategoryMapJpaEntity toJpaEntity(RestaurantCategoryMap domain);

    void updateEntity(@MappingTarget RestaurantCategoryMapJpaEntity entity, RestaurantCategoryMap domain);
}
