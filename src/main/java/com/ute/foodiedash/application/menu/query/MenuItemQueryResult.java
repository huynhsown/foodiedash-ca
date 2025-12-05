package com.ute.foodiedash.application.menu.query;

import com.ute.foodiedash.domain.menu.enums.MenuItemStatus;
import java.math.BigDecimal;
import java.util.List;

public record MenuItemQueryResult(
    Long id,
    Long menuId,
    Long restaurantId,
    String name,
    String description,
    BigDecimal price,
    String imageUrl,
    MenuItemStatus status,
    List<MenuItemOptionQueryResult> itemOptions
) {}
