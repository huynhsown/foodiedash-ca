package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.MenuItemOptionValueJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuItemOptionValueJpaMapper {
    MenuItemOptionValue toDomain(MenuItemOptionValueJpaEntity jpaEntity);
    MenuItemOptionValueJpaEntity toJpaEntity(MenuItemOptionValue domain);
}
