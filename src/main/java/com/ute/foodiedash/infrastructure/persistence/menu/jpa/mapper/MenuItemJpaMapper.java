package com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MenuItemOptionJpaMapper.class})
public interface MenuItemJpaMapper {

    MenuItem toDomain(MenuItemJpaEntity jpaEntity);

    MenuItemJpaEntity toJpaEntity(MenuItem domain);

    @AfterMapping
    default void setMenuItemReferences(@MappingTarget MenuItemJpaEntity jpaEntity) {
        if (jpaEntity.getOptions() != null && !jpaEntity.getOptions().isEmpty()) {
            for (var option : jpaEntity.getOptions()) {
                option.setMenuItem(jpaEntity);
            }
        }
    }
}
