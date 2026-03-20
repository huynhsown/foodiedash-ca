package com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.MenuItemOption;
import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuItemOptionValueJpaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {MenuItemOptionValueJpaMapper.class})
public abstract class MenuItemOptionJpaMapper {

    @Autowired
    protected MenuItemOptionValueJpaMapper menuItemOptionValueJpaMapper;

    public MenuItemOption toDomain(MenuItemOptionJpaEntity e) {
        if (e == null) {
            return null;
        }

        var values = e.getValues() == null
                ? java.util.Collections.<MenuItemOptionValue>emptyList()
                : e.getValues().stream()
                    .map(menuItemOptionValueJpaMapper::toDomain)
                    .toList();

        return MenuItemOption.reconstruct(
                e.getId(),
                e.getMenuItem() != null ? e.getMenuItem().getId() : null,
                e.getName(),
                e.getRequired(),
                e.getMinValue(),
                e.getMaxValue(),
                values,
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getCreatedBy(),
                e.getUpdatedBy(),
                e.getDeletedAt(),
                e.getVersion()
        );
    }

    @Mapping(target = "menuItem", ignore = true)
    public abstract MenuItemOptionJpaEntity toJpaEntity(MenuItemOption domain);

    public void updateEntity(@MappingTarget MenuItemOptionJpaEntity e, MenuItemOption domain) {
        e.setName(domain.getName());
        e.setRequired(domain.getRequired());
        e.setMinValue(domain.getMinValue());
        e.setMaxValue(domain.getMaxValue());
        mergeValues(e, domain);
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }

    private void mergeValues(MenuItemOptionJpaEntity e, MenuItemOption domain) {
        if (domain.getValues() == null) {
            e.getValues().clear();
            return;
        }

        Set<Long> domainValueIds = domain.getValues().stream()
                .map(MenuItemOptionValue::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        e.getValues().removeIf(v -> !domainValueIds.contains(v.getId()));

        Map<Long, MenuItemOptionValueJpaEntity> existingById = e.getValues().stream()
                .collect(Collectors.toMap(MenuItemOptionValueJpaEntity::getId, Function.identity()));

        for (MenuItemOptionValue domainVal : domain.getValues()) {
            if (domainVal.getId() != null && existingById.containsKey(domainVal.getId())) {
                menuItemOptionValueJpaMapper.updateEntity(existingById.get(domainVal.getId()), domainVal);
            } else {
                MenuItemOptionValueJpaEntity jpaVal = menuItemOptionValueJpaMapper.toJpaEntity(domainVal);
                jpaVal.setOption(e);
                e.getValues().add(jpaVal);
            }
        }
    }

    @AfterMapping
    protected void setMenuItemOptionReferences(@MappingTarget MenuItemOptionJpaEntity jpaEntity) {
        if (jpaEntity.getValues() != null && !jpaEntity.getValues().isEmpty()) {
            jpaEntity.getValues().forEach(v -> v.setOption(jpaEntity));
        }
    }
}
