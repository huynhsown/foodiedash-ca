package com.ute.foodiedash.application.user.port;

import com.ute.foodiedash.domain.user.enums.RoleName;

import java.util.List;

public interface UserPermissionResolutionPort {

    List<String> resolvePermissionNames(List<RoleName> userRoleNames);
}
