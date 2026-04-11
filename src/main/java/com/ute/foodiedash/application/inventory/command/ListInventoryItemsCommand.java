package com.ute.foodiedash.application.inventory.command;

import com.ute.foodiedash.domain.inventory.enums.InventoryStatus;

public record ListInventoryItemsCommand(
        Long userId,
        Long restaurantId,
        Long id,
        String keyword,
        InventoryStatus status,
        Boolean lowStock,
        Integer page,
        Integer size,
        String sortBy,
        String sortDirection
) {}
