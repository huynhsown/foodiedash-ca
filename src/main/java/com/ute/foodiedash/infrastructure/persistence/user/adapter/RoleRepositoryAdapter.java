package com.ute.foodiedash.infrastructure.persistence.user.adapter;

import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.user.enums.RoleName;
import com.ute.foodiedash.domain.user.model.Role;
import com.ute.foodiedash.domain.user.repository.RoleRepository;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.PermissionJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.RoleJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.RolePermissionAssignmentJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper.RoleJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.repository.PermissionJpaRepository;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.repository.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final RoleJpaMapper roleJpaMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findById(Long id) {
        return roleJpaRepository.findByIdWithAssignments(id).map(roleJpaMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findByName(RoleName name) {
        return roleJpaRepository.findByNameWithAssignments(name).map(roleJpaMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleJpaRepository.findAllWithAssignments().stream()
                .map(roleJpaMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public Role save(Role role) {
        RoleJpaEntity entity;
        if (role.getId() == null) {
            entity = roleJpaMapper.toJpaEntity(role);
            replaceAssignments(entity, role);
        } else {
            entity = roleJpaRepository.findByIdWithAssignments(role.getId())
                    .orElseThrow(() -> new NotFoundException("Role not found"));
            roleJpaMapper.updateJpaEntity(role, entity);
            entity.getPermissionAssignments().clear();
            replaceAssignments(entity, role);
        }

        RoleJpaEntity saved = roleJpaRepository.save(entity);
        return roleJpaRepository.findByIdWithAssignments(saved.getId())
                .map(roleJpaMapper::toDomain)
                .orElseGet(() -> roleJpaMapper.toDomain(saved));
    }

    private void replaceAssignments(RoleJpaEntity entity, Role role) {
        for (Long permissionId : role.getPermissionIds()) {
            PermissionJpaEntity permissionRef = permissionJpaRepository.getReferenceById(permissionId);
            RolePermissionAssignmentJpaEntity row = new RolePermissionAssignmentJpaEntity();
            row.setRole(entity);
            row.setPermission(permissionRef);
            entity.getPermissionAssignments().add(row);
        }
    }
}
