package com.ute.foodiedash.application.restaurant.query;

import java.time.LocalTime;

public record RestaurantBusinessHourQueryResult(
    Long id,
    Long restaurantId,
    Integer dayOfWeek,
    LocalTime openTime,
    LocalTime closeTime
) {}
