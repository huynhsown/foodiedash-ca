package com.ute.foodiedash.infrastructure.persistence.order.jpa.repository;

import com.ute.foodiedash.application.order.port.model.OrderValidation;
import com.ute.foodiedash.infrastructure.persistence.order.jpa.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
        WHERE o.id = :id
        AND o.customerId = :customerId
        AND o.deletedAt IS NULL
    """)
    Optional<OrderJpaEntity> findDetailByIdAndCustomerId(
            @Param("id") Long id,
            @Param("customerId") Long customerId
    );

    @Query("""
        SELECT DISTINCT o FROM OrderJpaEntity o
        LEFT JOIN FETCH o.items i
        WHERE o.code = :code
        AND o.deletedAt IS NULL
    """)
    Optional<OrderJpaEntity> findDetailByCode(@Param("code") String code);

    @Query("""
        SELECT DISTINCT o FROM OrderJpaEntity o
        LEFT JOIN FETCH o.items i
        WHERE o.id IN :ids
        AND o.deletedAt IS NULL
    """)
    List<OrderJpaEntity> findDetailsByIdIn(@Param("ids") List<Long> ids);

    @Query("""
        SELECT o.id, i.menuItemId, i.name
        FROM OrderJpaEntity o
        JOIN o.items i
        WHERE o.id IN :ids AND o.deletedAt IS NULL
    """)
    List<Object[]> findBasicInfoByIdIn(@Param("ids") List<Long> ids);

    @Query("""
        SELECT o FROM OrderJpaEntity o
        WHERE o.customerId = :customerId
        AND o.deletedAt IS NULL
        ORDER BY o.placedAt DESC
    """)
    List<OrderJpaEntity> findSummariesByCustomerId(@Param("customerId") Long customerId);

    @Query("""
        SELECT new com.ute.foodiedash.application.order.port.model.OrderValidation(
            o.id,
            o.customerId,
            o.restaurantId,
            o.status
        )
        FROM OrderJpaEntity o
        WHERE o.id = :orderId
    """)
    Optional<OrderValidation> findForReview(Long orderId);

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
