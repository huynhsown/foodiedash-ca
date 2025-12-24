package com.ute.foodiedash.infrastructure.persistence.order.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {

    @Query("""
        SELECT DISTINCT o FROM OrderJpaEntity o
        LEFT JOIN FETCH o.items i
        WHERE o.id = :id
        AND o.deletedAt IS NULL
    """)
    Optional<OrderJpaEntity> findDetailById(@Param("id") Long id);

    @Query("""
        SELECT DISTINCT o FROM OrderJpaEntity o
        LEFT JOIN FETCH o.items i
        WHERE o.code = :code
        AND o.deletedAt IS NULL
    """)
    Optional<OrderJpaEntity> findDetailByCode(@Param("code") String code);

    @Modifying
    @Query("""
        UPDATE OrderJpaEntity
        SET deletedAt = CURRENT_TIMESTAMP
        WHERE id = :id
    """)
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("""
        UPDATE OrderJpaEntity
        SET deletedAt = NULL
        WHERE id = :id
    """)
    void restoreById(@Param("id") Long id);
}
