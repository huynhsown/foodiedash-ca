package com.ute.foodiedash.domain.user.repository;

import com.ute.foodiedash.domain.user.enums.RoleName;
import com.ute.foodiedash.domain.user.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findById(Long id);

    Optional<Role> findByName(RoleName name);

    List<Role> findAll();

    Role save(Role role);
}
