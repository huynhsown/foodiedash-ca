package com.ute.foodiedash.application.restaurant.query;

import java.util.List;

public record RestaurantCategoriesPageResult(
    List<RestaurantCategoryQueryResult> content,
    int number,
    int size,
    long totalElements,
    int totalPages,
    boolean empty,
    int numberOfElements,
    boolean hasNextPage,
    boolean hasPreviousPage
) {}
