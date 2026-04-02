package com.ute.foodiedash.infrastructure.persistence.user.adapter;

import com.ute.foodiedash.domain.user.model.Permission;
import com.ute.foodiedash.domain.user.repository.PermissionRepository;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper.PermissionJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.repository.PermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;
    private final PermissionJpaMapper permissionJpaMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Permission> findById(Long id) {
        return permissionJpaRepository.findById(id).map(permissionJpaMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        return permissionJpaRepository.findAll().stream()
                .map(permissionJpaMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> findAllByIdIn(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return permissionJpaRepository.findAllById(ids).stream()
                .map(permissionJpaMapper::toDomain)
                .toList();
    }
}
