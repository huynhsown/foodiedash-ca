package com.ute.foodiedash.application.restaurant.query;

import java.time.Instant;

public record RestaurantPauseQueryResult(
    Long id,
    Long restaurantId,
    String reason,
    Instant pausedFrom,
    Instant pausedTo
) {}
