package com.ute.foodiedash.application.inventory.query;

import com.ute.foodiedash.domain.inventory.enums.InventoryStatus;
import com.ute.foodiedash.domain.inventory.enums.InventoryUnit;

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
) {}
