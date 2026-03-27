package com.ute.foodiedash.infrastructure.persistence.cart.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.cart.jpa.entity.CartItemOptionValueJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemOptionValueJpaRepository extends JpaRepository<CartItemOptionValueJpaEntity, Long> {
    List<CartItemOptionValueJpaEntity> findByCartItemOptionIdAndDeletedAtIsNull(Long cartItemOptionId);
    
    List<CartItemOptionValueJpaEntity> findByCartItemOptionIdInAndDeletedAtIsNull(List<Long> cartItemOptionIds);

    @Query("""
        SELECT c FROM CartItemOptionValueJpaEntity c
        WHERE c.cartItemOption.id IN :cartItemOptionIds
          AND c.deletedAt IS NULL
    """)
    List<CartItemOptionValueJpaEntity> findByCartItemOptionIds(
        @Param("cartItemOptionIds") List<Long> cartItemOptionIds
    );

    @Modifying
    @Query("""
        UPDATE CartItemOptionValueJpaEntity
        SET deletedAt = CURRENT_TIMESTAMP
        WHERE id = :id
    """)
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("""
        UPDATE CartItemOptionValueJpaEntity
        SET deletedAt = NULL
        WHERE id = :id
    """)
    void restoreById(@Param("id") Long id);
}
