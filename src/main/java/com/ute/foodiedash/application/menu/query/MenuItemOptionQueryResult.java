package com.ute.foodiedash.application.menu.query;

import java.util.List;

public record MenuItemOptionQueryResult(
    Long id,
    Long menuItemId,
    String name,
    Boolean required,
    Integer minValue,
    Integer maxValue,
    List<MenuItemOptionValueQueryResult> itemOptionValues
) {}
