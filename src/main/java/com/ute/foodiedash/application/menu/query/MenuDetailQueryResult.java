package com.ute.foodiedash.application.menu.query;

import java.time.LocalTime;
import java.util.List;

public record MenuDetailQueryResult(
    Long id,
    String name,
    LocalTime startTime,
    LocalTime endTime,
    boolean isAvailable,
    List<MenuItemQueryResult> menuItems
) {}
