package com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionValueJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuItemOptionValueJpaMapper {

    @Mapping(target = "optionId", source = "option.id")
    MenuItemOptionValue toDomain(MenuItemOptionValueJpaEntity jpaEntity);

    @Mapping(target = "option", ignore = true)
    MenuItemOptionValueJpaEntity toJpaEntity(MenuItemOptionValue domain);
}
