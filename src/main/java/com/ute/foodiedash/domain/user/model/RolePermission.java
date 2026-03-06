package com.ute.foodiedash.domain.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermission {

    private Long roleId;
    private Long permissionId;

    public static RolePermission of(Long roleId, Long permissionId) {
        RolePermission rp = new RolePermission();
        rp.roleId = roleId;
        rp.permissionId = permissionId;
        return rp;
    }
}
