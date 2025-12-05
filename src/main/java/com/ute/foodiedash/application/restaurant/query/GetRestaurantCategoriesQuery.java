package com.ute.foodiedash.application.restaurant.query;

public record GetRestaurantCategoriesQuery(
    int page,
    int size,
    String sortBy,
    boolean ascending
) {}
