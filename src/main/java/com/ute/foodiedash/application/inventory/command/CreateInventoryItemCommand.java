package com.ute.foodiedash.application.inventory.command;

import com.ute.foodiedash.domain.inventory.enums.InventoryUnit;

import java.math.BigDecimal;

public record CreateInventoryItemCommand(
        Long restaurantId,
        String sku,
        String name,
        InventoryUnit unit,
        BigDecimal reorderLevel,
        BigDecimal reorderQuantity,
        BigDecimal unitCost
) {}
