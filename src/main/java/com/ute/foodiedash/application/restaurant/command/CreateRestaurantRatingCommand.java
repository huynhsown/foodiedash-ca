package com.ute.foodiedash.application.restaurant.command;

import java.math.BigDecimal;

public record CreateRestaurantRatingCommand(
    Long restaurantId,
    BigDecimal ratingAvg,
    Integer ratingCount
) {}
