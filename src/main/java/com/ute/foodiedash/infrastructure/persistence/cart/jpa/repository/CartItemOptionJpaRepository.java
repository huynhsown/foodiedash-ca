package com.ute.foodiedash.infrastructure.persistence.cart.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemOptionJpaRepository extends JpaRepository<CartItemOptionJpaEntity, Long> {
    List<CartItemOptionJpaEntity> findByCartItemIdAndDeletedAtIsNull(Long cartItemId);
    
    List<CartItemOptionJpaEntity> findByCartItemIdInAndDeletedAtIsNull(List<Long> cartItemIds);

    @Modifying
    @Query("""
        UPDATE CartItemOptionJpaEntity
        SET deletedAt = CURRENT_TIMESTAMP
        WHERE id = :id
    """)
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("""
        UPDATE CartItemOptionJpaEntity
        SET deletedAt = NULL
        WHERE id = :id
    """)
    void restoreById(@Param("id") Long id);
}
