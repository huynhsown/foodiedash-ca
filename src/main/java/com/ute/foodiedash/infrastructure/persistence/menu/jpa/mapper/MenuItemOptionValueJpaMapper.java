package com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionValueJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MenuItemOptionValueJpaMapper {

    default MenuItemOptionValue toDomain(MenuItemOptionValueJpaEntity e) {
        if (e == null) {
            return null;
        }
        return MenuItemOptionValue.reconstruct(
                e.getId(),
                e.getOption() != null ? e.getOption().getId() : null,
                e.getName(),
                e.getExtraPrice(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getCreatedBy(),
                e.getUpdatedBy(),
                e.getDeletedAt(),
                e.getVersion()
        );
    }

    @Mapping(target = "option", ignore = true)
    MenuItemOptionValueJpaEntity toJpaEntity(MenuItemOptionValue domain);

    default void updateEntity(@MappingTarget MenuItemOptionValueJpaEntity e, MenuItemOptionValue domain) {
        e.setName(domain.getName());
        e.setExtraPrice(domain.getExtraPrice());
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }
}
