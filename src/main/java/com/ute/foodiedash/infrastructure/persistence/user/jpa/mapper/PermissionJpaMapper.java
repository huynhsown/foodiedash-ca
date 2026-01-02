package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.Permission;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.PermissionJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionJpaMapper {

    default Permission toDomain(PermissionJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Permission.reconstruct(
                entity.getId(),
                entity.getName(),
                entity.getModule(),
                entity.getDescription()
        );
    }

    default PermissionJpaEntity toJpaEntity(Permission domain) {
        if (domain == null) {
            return null;
        }
        PermissionJpaEntity entity = new PermissionJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setModule(domain.getModule());
        entity.setDescription(domain.getDescription());
        return entity;
    }
}
