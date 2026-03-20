package com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.mapper;

import com.ute.foodiedash.domain.restaurant.model.RestaurantPreparationSetting;
import com.ute.foodiedash.infrastructure.persistence.restaurant.jpa.entity.RestaurantPreparationSettingJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RestaurantPreparationSettingJpaMapper {
    RestaurantPreparationSetting toDomain(RestaurantPreparationSettingJpaEntity jpaEntity);
    RestaurantPreparationSettingJpaEntity toJpaEntity(RestaurantPreparationSetting domain);

    void updateEntity(@MappingTarget RestaurantPreparationSettingJpaEntity entity, RestaurantPreparationSetting domain);
}
