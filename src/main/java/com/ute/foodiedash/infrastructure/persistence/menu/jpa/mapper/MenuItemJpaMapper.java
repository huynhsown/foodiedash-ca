package com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.model.MenuItemOption;
import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionValueJpaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {MenuItemOptionJpaMapper.class})
public abstract class MenuItemJpaMapper {

    @Autowired
    protected MenuItemOptionJpaMapper menuItemOptionJpaMapper;

    public MenuItem toDomain(MenuItemJpaEntity e) {
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

    private MenuItemOption optionToDomain(MenuItemOptionJpaEntity o) {
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

    private MenuItemOptionValue valueToDomain(MenuItemOptionValueJpaEntity v) {
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

    public abstract MenuItemJpaEntity toJpaEntity(MenuItem domain);

    public void updateEntity(@MappingTarget MenuItemJpaEntity e, MenuItem domain) {
        e.setMenuId(domain.getMenuId());
        e.setRestaurantId(domain.getRestaurantId());
        e.setName(domain.getName());
        e.setDescription(domain.getDescription());
        e.setPrice(domain.getPrice());
        e.setImageUrl(domain.getImageUrl());
        e.setStatus(domain.getStatus());
        mergeOptions(e, domain);
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }

    private void mergeOptions(MenuItemJpaEntity e, MenuItem domain) {
        if (domain.getOptions() == null) {
            e.getOptions().clear();
            return;
        }

        Set<Long> domainOptionIds = domain.getOptions().stream()
                .map(MenuItemOption::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        e.getOptions().removeIf(opt -> !domainOptionIds.contains(opt.getId()));

        Map<Long, MenuItemOptionJpaEntity> existingById = e.getOptions().stream()
                .collect(Collectors.toMap(MenuItemOptionJpaEntity::getId, Function.identity()));

        for (MenuItemOption domainOpt : domain.getOptions()) {
            if (domainOpt.getId() != null && existingById.containsKey(domainOpt.getId())) {
                menuItemOptionJpaMapper.updateEntity(existingById.get(domainOpt.getId()), domainOpt);
            } else {
                MenuItemOptionJpaEntity jpaOpt = menuItemOptionJpaMapper.toJpaEntity(domainOpt);
                jpaOpt.setMenuItem(e);
                e.getOptions().add(jpaOpt);
            }
        }
    }

    @AfterMapping
    protected void setMenuItemReferences(@MappingTarget MenuItemJpaEntity jpaEntity) {
        if (jpaEntity.getOptions() != null && !jpaEntity.getOptions().isEmpty()) {
            jpaEntity.getOptions().forEach(o -> o.setMenuItem(jpaEntity));
        }
    }
}