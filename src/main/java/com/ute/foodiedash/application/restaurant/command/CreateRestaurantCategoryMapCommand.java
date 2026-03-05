package com.ute.foodiedash.application.restaurant.command;

public record CreateRestaurantCategoryMapCommand(
    Long restaurantId,
    Long categoryId
) {}
