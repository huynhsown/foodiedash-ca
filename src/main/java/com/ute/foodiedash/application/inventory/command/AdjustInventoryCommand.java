package com.ute.foodiedash.application.inventory.command;

import java.math.BigDecimal;

public record AdjustInventoryCommand(
        Long userId,
        Long id,
        BigDecimal quantityChange,
        String note
) {}
