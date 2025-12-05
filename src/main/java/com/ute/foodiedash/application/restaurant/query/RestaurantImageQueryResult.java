package com.ute.foodiedash.application.restaurant.query;

public record RestaurantImageQueryResult(
    Long id,
    Long restaurantId,
    String imageUrl
) {}
