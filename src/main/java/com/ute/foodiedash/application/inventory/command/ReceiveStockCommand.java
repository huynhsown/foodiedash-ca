package com.ute.foodiedash.application.inventory.command;

import java.math.BigDecimal;

public record ReceiveStockCommand(
        Long id,
        BigDecimal quantity,
        Long purchaseOrderId,
        String note
) {}
