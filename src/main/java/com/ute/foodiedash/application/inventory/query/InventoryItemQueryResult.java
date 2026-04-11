package com.ute.foodiedash.application.inventory.query;

import com.ute.foodiedash.domain.inventory.enums.InventoryStatus;
import com.ute.foodiedash.domain.inventory.enums.InventoryUnit;
import com.ute.foodiedash.domain.inventory.model.InventoryItem;

import java.math.BigDecimal;

public record InventoryItemQueryResult(
        Long id,
        Long restaurantId,
        String sku,
        String name,
        InventoryUnit unit,
        BigDecimal quantityOnHand,
        BigDecimal reorderLevel,
        BigDecimal reorderQuantity,
        BigDecimal unitCost,
        InventoryStatus status,
        boolean lowStock
) {
    public static InventoryItemQueryResult from(InventoryItem item) {
        return new InventoryItemQueryResult(
                item.getId(),
                item.getRestaurantId(),
                item.getSku(),
                item.getName(),
                item.getUnit(),
                item.getQuantityOnHand(),
                item.getReorderLevel(),
                item.getReorderQuantity(),
                item.getUnitCost(),
                item.getStatus(),
                item.isLowStock()
        );
    }
}
