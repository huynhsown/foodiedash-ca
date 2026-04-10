package com.ute.foodiedash.application.inventory.command;

import java.math.BigDecimal;

public record MarkWasteCommand(
        Long id,
        BigDecimal quantity,
        String reason
) {}
