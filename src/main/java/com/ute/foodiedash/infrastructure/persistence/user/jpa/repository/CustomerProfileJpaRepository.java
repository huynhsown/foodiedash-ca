package com.ute.foodiedash.infrastructure.persistence.user.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.CustomerProfileJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerProfileJpaRepository extends JpaRepository<CustomerProfileJpaEntity, Long> {
    Optional<CustomerProfileJpaEntity> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
