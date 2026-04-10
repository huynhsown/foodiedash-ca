package com.ute.foodiedash.application.inventory.command;

import java.math.BigDecimal;

public record ReturnStockCommand(
        Long id,
        BigDecimal quantity,
        Long orderId,
        String note
) {}
