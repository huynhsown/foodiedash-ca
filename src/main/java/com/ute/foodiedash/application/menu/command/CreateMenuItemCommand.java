package com.ute.foodiedash.application.menu.command;

import java.math.BigDecimal;

public record CreateMenuItemCommand(
    Long menuId,
    String name,
    String description,
    BigDecimal price,
    String imageUrl
) {}
