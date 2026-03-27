package com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.model.MenuItemOption;
import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionValueJpaEntity;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MenuItemOptionJpaMapper.class})
public interface MenuItemJpaMapper {

    default MenuItem toDomain(MenuItemJpaEntity e) {
        if (e == null) return null;
        List<MenuItemOption> options = e.getOptions() != null
                ? e.getOptions().stream()
                .map(this::optionToDomain)
                .collect(Collectors.toList())
                : Collections.emptyList();
        return MenuItem.reconstruct(
                e.getId(),
                e.getMenuId(),
                e.getRestaurantId(),
                e.getName(),
                e.getDescription(),
                e.getPrice(),
                e.getImageUrl(),
                e.getStatus(),
                options,
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getCreatedBy(),
                e.getUpdatedBy(),
                e.getDeletedAt(),
                e.getVersion()
        );
    }

    default MenuItemOption optionToDomain(MenuItemOptionJpaEntity o) {
        if (o == null) return null;
        Long menuItemId = o.getMenuItem() != null ? o.getMenuItem().getId() : null;
        List<MenuItemOptionValue> values = o.getValues() != null
                ? o.getValues().stream()
                .map(this::valueToDomain)
                .collect(Collectors.toList())
                : Collections.emptyList();
        return MenuItemOption.reconstruct(
                o.getId(),
                menuItemId,
                o.getName(),
                o.getRequired(),
                o.getMinValue(),
                o.getMaxValue(),
                values,
                o.getCreatedAt(),
                o.getUpdatedAt(),
                o.getCreatedBy(),
                o.getUpdatedBy(),
                o.getDeletedAt(),
                o.getVersion()
        );
    }

    default MenuItemOptionValue valueToDomain(MenuItemOptionValueJpaEntity v) {
        if (v == null) return null;
        Long optionId = v.getOption() != null ? v.getOption().getId() : null;
        return MenuItemOptionValue.reconstruct(
                v.getId(),
                optionId,
                v.getName(),
                v.getExtraPrice(),
                v.getCreatedAt(),
                v.getUpdatedAt(),
                v.getCreatedBy(),
                v.getUpdatedBy(),
                v.getDeletedAt(),
                v.getVersion()
        );
    }

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