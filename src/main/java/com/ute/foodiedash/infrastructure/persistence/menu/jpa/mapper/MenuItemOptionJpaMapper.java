package com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.MenuItemOption;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MenuItemOptionValueJpaMapper.class})
public interface MenuItemOptionJpaMapper {

    @Mapping(target = "menuItemId", source = "menuItem.id")
    MenuItemOption toDomain(MenuItemOptionJpaEntity jpaEntity);

    @Mapping(target = "menuItem", ignore = true)
    MenuItemOptionJpaEntity toJpaEntity(MenuItemOption domain);

    @AfterMapping
    default void setMenuItemOptionReferences(@MappingTarget MenuItemOptionJpaEntity jpaEntity) {
        if (jpaEntity.getValues() != null && !jpaEntity.getValues().isEmpty()) {
            for (var value : jpaEntity.getValues()) {
                value.setOption(jpaEntity);
            }
        }
    }
}
