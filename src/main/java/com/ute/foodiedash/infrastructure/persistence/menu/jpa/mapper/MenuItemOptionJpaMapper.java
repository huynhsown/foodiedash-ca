package com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.MenuItemOption;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuItemOptionJpaMapper {
    MenuItemOption toDomain(MenuItemOptionJpaEntity jpaEntity);
    MenuItemOptionJpaEntity toJpaEntity(MenuItemOption domain);
}
