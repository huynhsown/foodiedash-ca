package com.ute.foodiedash.application.restaurant.command;

public record CreateRestaurantCategoryCommand(
    String name,
    String iconUrl,
    String description
) {}
