package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.user.enums.RoleName;
import lombok.Getter;

@Getter
public class UserRole {

    private Long userId;
    private RoleName roleName;

    private UserRole() {}

    public static UserRole of(Long userId, RoleName roleName) {
        UserRole ur = new UserRole();
        ur.userId = userId;
        ur.roleName = roleName;
        return ur;
    }

    public static UserRole create(RoleName roleName) {
        UserRole ur = new UserRole();
        ur.roleName = roleName;
        return ur;
    }

    public static UserRole reconstruct(Long userId, RoleName roleName) {
        return of(userId, roleName);
    }
}
