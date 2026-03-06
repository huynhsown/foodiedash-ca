package com.ute.foodiedash.domain.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permission {
    private Long id;
    private String name;
    private String module;
    private String description;

    public boolean belongsToModule(String module) {
        return this.module.equals(module);
    }
}
