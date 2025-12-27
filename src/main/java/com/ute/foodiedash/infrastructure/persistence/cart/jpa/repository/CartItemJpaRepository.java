package com.ute.foodiedash.infrastructure.persistence.cart.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemJpaRepository extends JpaRepository<CartItemJpaEntity, Long> {
    @Query("""
        SELECT coalesce(SUM(ci.quantity), 0)
        FROM CartItemJpaEntity ci
        WHERE ci.cart.id IN :cartIds
        AND ci.deletedAt IS NULL
    """)
    Integer sumQuantityByCartIds(@Param("cartIds") List<Long> cartIds);

    List<CartItemJpaEntity> findByCartIdAndDeletedAtIsNull(Long cartId);

    @Query("""
        SELECT c FROM CartItemJpaEntity c
        WHERE c.cart.id = :cartId
            AND c.deletedAt IS NULL
    """)
    List<CartItemJpaEntity> findByCartId(@Param("cartId") Long cartId);

    @Query("""
        SELECT c FROM CartItemJpaEntity c
        WHERE c.cart.id IN :cartItemIds
            AND c.deletedAt IS NULL
    """)
    List<CartItemJpaEntity> findByCartItemIdIn(@Param("cartItemIds") List<Long> cartItemIds);

    @Modifying
    @Query("""
        UPDATE CartItemJpaEntity
        SET deletedAt = CURRENT_TIMESTAMP
        WHERE id = :id
    """)
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("""
        UPDATE CartItemJpaEntity
        SET deletedAt = NULL
        WHERE id = :id
    """)
    void restoreById(@Param("id") Long id);
}
