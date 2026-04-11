package com.ute.foodiedash.application.inventory.command;

import com.ute.foodiedash.domain.inventory.enums.InventoryStatus;

public record ChangeInventoryStatusCommand(
        Long userId,
        Long id,
        InventoryStatus status
) {}
