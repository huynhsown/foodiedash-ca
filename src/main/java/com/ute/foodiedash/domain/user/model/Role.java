package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.user.enums.RoleName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Role {

    @Getter
    private Long id;
    @Getter
    private RoleName name;
    private final List<RolePermission> rolePermissions = new ArrayList<>();

    public static Role reconstruct(Long id, RoleName name, List<RolePermission> permissions) {
        if (name == null) {
            throw new BadRequestException("Role name required");
        }
        Role role = new Role();
        role.id = id;
        role.name = name;
        if (permissions != null && !permissions.isEmpty()) {
            role.rolePermissions.addAll(permissions);
        }
        return role;
    }

    public static Role reconstructWithPermissionIds(Long id, RoleName name, List<Long> permissionIds) {
        if (name == null) {
            throw new BadRequestException("Role name required");
        }
        Role role = new Role();
        role.id = id;
        role.name = name;
        if (permissionIds != null) {
            for (Long pid : permissionIds) {
                if (pid != null) {
                    role.rolePermissions.add(RolePermission.associate(pid));
                }
            }
        }
        return role;
    }

    public List<RolePermission> getRolePermissions() {
        return Collections.unmodifiableList(rolePermissions);
    }

    public List<Long> getPermissionIds() {
        return rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());
    }

    public boolean hasName(String roleName) {
        return name != null && name.name().equals(roleName);
    }

    public boolean hasPermission(Long permissionId) {
        if (permissionId == null) {
            return false;
        }
        return rolePermissions.stream()
                .anyMatch(rp -> Objects.equals(rp.getPermissionId(), permissionId));
    }

    public void grantPermission(Long permissionId) {
        if (hasPermission(permissionId)) {
            throw new BadRequestException("Permission already granted to this role");
        }
        rolePermissions.add(RolePermission.associate(permissionId));
    }

    public void revokePermission(Long permissionId) {
        if (permissionId == null) {
            throw new BadRequestException("permissionId required");
        }
        boolean removed = rolePermissions.removeIf(rp -> Objects.equals(rp.getPermissionId(), permissionId));
        if (!removed) {
            throw new BadRequestException("Permission is not assigned to this role");
        }
    }
}
