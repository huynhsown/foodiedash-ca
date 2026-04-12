package com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper;

import com.ute.foodiedash.domain.user.model.Role;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.RoleJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.RolePermissionAssignmentJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleJpaMapper {

    @Mapping(target = "permissionAssignments", ignore = true)
    RoleJpaEntity toJpaEntity(Role domain);

    @Mapping(target = "permissionAssignments", ignore = true)
    void updateJpaEntity(Role domain, @MappingTarget RoleJpaEntity entity);

    default Role toDomain(RoleJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        List<Long> permissionIds = new ArrayList<>();
        if (entity.getPermissionAssignments() != null) {
            for (RolePermissionAssignmentJpaEntity a : entity.getPermissionAssignments()) {
                if (a.getPermission() != null && a.getPermission().getId() != null) {
                    permissionIds.add(a.getPermission().getId());
                }
            }
        }
        return Role.reconstructWithPermissionIds(entity.getId(), entity.getName(), permissionIds);
    }
}
