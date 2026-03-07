package com.ute.foodiedash.infrastructure.persistence.cart.jpa.repository;

import com.ute.foodiedash.domain.cart.enums.CartStatus;
import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartJpaRepository extends JpaRepository<CartJpaEntity, Long> {
    List<CartJpaEntity> findByUserIdAndStatusAndDeletedAtIsNull(Long userId, CartStatus status);

    @Query("""
        SELECT c FROM CartJpaEntity c
        WHERE c.userId = :userId
            AND c.restaurantId = :restaurantId
            AND c.status = 'ACTIVE'
            AND c.deletedAt IS NULL
            AND c.expiresAt >= CURRENT_TIMESTAMP
    """)
    Optional<CartJpaEntity> findActiveCart(
        @Param("userId") Long userId,
        @Param("restaurantId") Long restaurantId
    );

    @Modifying
    @Query("""
        UPDATE CartJpaEntity c
        SET c.deletedAt = CURRENT_TIMESTAMP
        WHERE c.id IN :cartIds
    """)
    void removeCarts(@Param("cartIds") List<Long> cartIds);

    @Modifying
    @Query("""
        UPDATE CartJpaEntity
        SET deletedAt = CURRENT_TIMESTAMP
        WHERE id = :id
    """)
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("""
        UPDATE CartJpaEntity
        SET deletedAt = NULL
        WHERE id = :id
    """)
    void restoreById(@Param("id") Long id);
}
