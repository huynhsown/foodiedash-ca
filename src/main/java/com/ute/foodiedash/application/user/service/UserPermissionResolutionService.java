package com.ute.foodiedash.application.user.service;

import com.ute.foodiedash.application.user.port.UserPermissionResolutionPort;
import com.ute.foodiedash.domain.user.enums.RoleName;
import com.ute.foodiedash.domain.user.model.Permission;
import com.ute.foodiedash.domain.user.repository.PermissionRepository;
import com.ute.foodiedash.domain.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserPermissionResolutionService implements UserPermissionResolutionPort {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<String> resolvePermissionNames(List<RoleName> userRoleNames) {
        if (userRoleNames == null || userRoleNames.isEmpty()) {
            return List.of();
        }
        Set<Long> permissionIds = new LinkedHashSet<>();
        for (RoleName roleName : userRoleNames) {
            roleRepository.findByName(roleName).ifPresent(role -> permissionIds.addAll(role.getPermissionIds()));
        }
        if (permissionIds.isEmpty()) {
            return List.of();
        }
        return permissionRepository.findAllByIdIn(permissionIds).stream()
                .map(Permission::getName)
                .filter(n -> n != null && !n.isBlank())
                .map(String::trim)
                .distinct()
                .sorted()
                .toList();
    }
}
