package com.ute.foodiedash.application.inventory.command;

import java.math.BigDecimal;

public record UpdateInventoryItemCommand(
        Long id,
        String name,
        BigDecimal unitCost,
        BigDecimal reorderLevel,
        BigDecimal reorderQuantity
) {}
