package com.ute.foodiedash.infrastructure.persistence.menu.jpa.mapper;

import com.ute.foodiedash.domain.menu.model.Menu;
import com.ute.foodiedash.infrastructure.persistence.menu.jpa.entity.MenuJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuJpaMapper {

    default Menu toDomain(MenuJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        return Menu.reconstruct(
                jpaEntity.getId(),
                jpaEntity.getRestaurantId(),
                jpaEntity.getName(),
                jpaEntity.getStartTime(),
                jpaEntity.getEndTime(),
                jpaEntity.getStatus(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getCreatedBy(),
                jpaEntity.getUpdatedBy(),
                jpaEntity.getDeletedAt(),
                jpaEntity.getVersion()
        );
    }

    MenuJpaEntity toJpaEntity(Menu domain);
}
