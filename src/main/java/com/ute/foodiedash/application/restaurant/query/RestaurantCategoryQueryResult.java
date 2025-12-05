package com.ute.foodiedash.application.restaurant.query;

public record RestaurantCategoryQueryResult(
    Long id,
    String name,
    String iconUrl,
    String description
) {}
