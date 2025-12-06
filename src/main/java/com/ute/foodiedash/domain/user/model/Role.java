package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.user.enums.RoleName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Role {

    private Long id;
    private RoleName name;

    public boolean hasName(String roleName) {
        return this.name.name().equals(roleName);
    }
}
