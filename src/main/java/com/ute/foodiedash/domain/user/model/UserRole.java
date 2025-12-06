package com.ute.foodiedash.domain.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRole {

    private Long userId;
    private Long roleId;

    public static UserRole of(Long userId, Long roleId) {
        UserRole ur = new UserRole();
        ur.userId = userId;
        ur.roleId = roleId;
        return ur;
    }
}
