package com.ute.foodiedash.infrastructure.persistence.inventory.jpa.repository;

import com.ute.foodiedash.infrastructure.persistence.inventory.jpa.entity.InventoryTransactionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryTransactionJpaRepository extends JpaRepository<InventoryTransactionJpaEntity, Long> {

    List<InventoryTransactionJpaEntity> findByInventoryItemIdOrderByCreatedAtAsc(Long inventoryItemId);
}
