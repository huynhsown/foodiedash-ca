package com.ute.foodiedash.domain.user.model;

import lombok.Getter;

/**
 * Danh mục quyền (catalog), không thuộc aggregate {@link Role}.
 * {@link Role} tham chiếu quyền qua {@link RolePermission#getPermissionId()}.
 */
@Getter
public class Permission {
    private Long id;
    private String name;
    private String module;
    private String description;

    public static Permission reconstruct(Long id, String name, String module, String description) {
        Permission p = new Permission();
        p.id = id;
        p.name = name;
        p.module = module;
        p.description = description;
        return p;
    }

    public boolean belongsToModule(String module) {
        return module != null && module.equals(this.module);
    }
}
