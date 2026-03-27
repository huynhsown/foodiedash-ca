package com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.MenuItemOption;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionValueJpaEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MenuItemOptionJpaMapper {

    default MenuItemOption toDomain(MenuItemOptionJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        var values = jpaEntity.getValues() == null ? null : jpaEntity.getValues().stream()
                .map(MenuItemOptionJpaMapper::mapValueToDomain)
                .toList();

        return MenuItemOption.reconstruct(
                jpaEntity.getId(),
                jpaEntity.getMenuItem() != null ? jpaEntity.getMenuItem().getId() : null,
                jpaEntity.getName(),
                jpaEntity.getRequired(),
                jpaEntity.getMinValue(),
                jpaEntity.getMaxValue(),
                values,
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getCreatedBy(),
                jpaEntity.getUpdatedBy(),
                jpaEntity.getDeletedAt(),
                jpaEntity.getVersion()
        );
    }

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

    private static com.ute.foodiedash.domain.menu.model.MenuItemOptionValue mapValueToDomain(
            MenuItemOptionValueJpaEntity jpaEntity
    ) {
        if (jpaEntity == null) {
            return null;
        }

        return com.ute.foodiedash.domain.menu.model.MenuItemOptionValue.reconstruct(
                jpaEntity.getId(),
                jpaEntity.getOption() != null ? jpaEntity.getOption().getId() : null,
                jpaEntity.getName(),
                jpaEntity.getExtraPrice(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getCreatedBy(),
                jpaEntity.getUpdatedBy(),
                jpaEntity.getDeletedAt(),
                jpaEntity.getVersion()
        );
    }
}
