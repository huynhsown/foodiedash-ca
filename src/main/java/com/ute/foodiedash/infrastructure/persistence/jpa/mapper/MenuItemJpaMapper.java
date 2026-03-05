package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.MenuItemJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuItemJpaMapper {
    MenuItem toDomain(MenuItemJpaEntity jpaEntity);
    MenuItemJpaEntity toJpaEntity(MenuItem domain);
}
