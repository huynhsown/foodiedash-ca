package com.ute.foodiedash.infrastructure.persistence.paymentmethod.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.paymentmethod.jpa.entity.PaymentMethodJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodJpaRepository extends JpaRepository<PaymentMethodJpaEntity, Long> {
    Optional<PaymentMethodJpaEntity> findByCode(String code);

    boolean existsByCode(String code);

    @Query("""
            SELECT pm
            FROM PaymentMethodJpaEntity pm
            WHERE pm.active = TRUE
            ORDER BY pm.type ASC, pm.code ASC
            """)
    List<PaymentMethodJpaEntity> findAllActive();
}

