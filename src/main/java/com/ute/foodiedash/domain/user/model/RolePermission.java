package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import lombok.Getter;

import java.util.Objects;

@Getter
public final class RolePermission {

    private final Long permissionId;

    RolePermission(Long permissionId) {
        if (permissionId == null) {
            throw new BadRequestException("permissionId required");
        }
        this.permissionId = permissionId;
    }

    static RolePermission associate(Long permissionId) {
        return new RolePermission(permissionId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RolePermission that)) {
            return false;
        }
        return Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(permissionId);
    }
}
