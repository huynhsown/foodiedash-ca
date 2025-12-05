package com.ute.foodiedash.application.menu.query;

import java.util.List;

public record RestaurantMenuQueryResult(
    List<MenuDetailQueryResult> menus
) {}
