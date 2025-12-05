package com.ute.foodiedash.application.restaurant.query;

import java.math.BigDecimal;

public record RestaurantRatingQueryResult(
    Long id,
    Long restaurantId,
    BigDecimal ratingAvg,
    Integer ratingCount
) {}
