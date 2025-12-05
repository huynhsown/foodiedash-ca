package com.ute.foodiedash.application.restaurant.command;

import java.time.Instant;

public record CreateRestaurantPauseCommand(
    Long restaurantId,
    String reason,
    Instant pausedFrom,
    Instant pausedTo
) {}
