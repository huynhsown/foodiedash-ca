package com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionValueJpaEntity;
import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MenuItemOptionValueJpaMapper {

    default MenuItemOptionValue toDomain(MenuItemOptionValueJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return MenuItemOptionValue.reconstruct(
                entity.getId(),
                entity.getOption() != null ? entity.getOption().getId() : null,
                entity.getName(),
                entity.getExtraPrice(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedBy(),
                entity.getDeletedAt(),
                entity.getVersion()
        );
    }

    @Mapping(target = "option", ignore = true)
    MenuItemOptionValueJpaEntity toJpaEntity(MenuItemOptionValue domain);

    @Mapping(target = "option", ignore = true)
    void updateEntity(@MappingTarget MenuItemOptionValueJpaEntity entity, MenuItemOptionValue domain);
}
