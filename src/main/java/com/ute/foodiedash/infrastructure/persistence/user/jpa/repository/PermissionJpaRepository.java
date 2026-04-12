package com.ute.foodiedash.infrastructure.persistence.user.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.PermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionJpaRepository extends JpaRepository<PermissionJpaEntity, Long> {
}
