package com.ute.foodiedash.domain.inventory.repository;

import com.ute.foodiedash.domain.inventory.model.InventoryItem;
import com.ute.foodiedash.domain.inventory.model.InventoryTransaction;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {
    InventoryItem save(InventoryItem item);

    Optional<InventoryItem> findById(Long id);

    List<InventoryItem> findByRestaurantId(Long restaurantId);

    Optional<InventoryItem> findBySkuAndRestaurantId(String sku, Long restaurantId);

    boolean existsBySkuAndRestaurantId(String sku, Long restaurantId);

    List<InventoryItem> findLowStockItems(Long restaurantId);

    void softDeleteById(Long id);

    void restoreById(Long id);

    InventoryTransaction saveTransaction(InventoryTransaction transaction);

    List<InventoryTransaction> findTransactionsByItemId(Long inventoryItemId);
}
