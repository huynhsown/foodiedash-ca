package com.ute.foodiedash.domain.user.repository;

import com.ute.foodiedash.domain.user.model.Permission;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PermissionRepository {

    Optional<Permission> findById(Long id);

    List<Permission> findAll();

    List<Permission> findAllByIdIn(Collection<Long> ids);
}
