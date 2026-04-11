package com.ute.foodiedash.infrastructure.persistence.inventory.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.inventory.jpa.entity.InventoryItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemJpaRepository extends JpaRepository<InventoryItemJpaEntity, Long> {

    List<InventoryItemJpaEntity> findByRestaurantId(Long restaurantId);

    Optional<InventoryItemJpaEntity> findBySkuAndRestaurantId(String sku, Long restaurantId);

    boolean existsBySkuAndRestaurantId(String sku, Long restaurantId);

    @Query("""
                SELECT i FROM InventoryItemJpaEntity i
                WHERE i.restaurantId = :restaurantId
                AND i.deletedAt IS NULL
                AND i.quantityOnHand <= i.reorderLevel
            """)
    List<InventoryItemJpaEntity> findLowStockItems(@Param("restaurantId") Long restaurantId);

    @Modifying
    @Query("""
                UPDATE InventoryItemJpaEntity
                SET deletedAt = CURRENT_TIMESTAMP
                WHERE id = :id
            """)
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("""
                UPDATE InventoryItemJpaEntity
                SET deletedAt = NULL
                WHERE id = :id
            """)
    void restoreById(@Param("id") Long id);
}
