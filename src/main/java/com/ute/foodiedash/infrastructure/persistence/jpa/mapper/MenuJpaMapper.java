package com.ute.foodiedash.infrastructure.persistence.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.Menu;
import com.ute.foodiedash.infrastructure.persistence.jpa.entity.MenuJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuJpaMapper {
    Menu toDomain(MenuJpaEntity jpaEntity);
    MenuJpaEntity toJpaEntity(Menu domain);
}
