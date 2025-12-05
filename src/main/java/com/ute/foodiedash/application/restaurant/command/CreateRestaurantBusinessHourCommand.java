package com.ute.foodiedash.application.restaurant.command;

import java.time.LocalTime;

public record CreateRestaurantBusinessHourCommand(
    Long restaurantId,
    Integer dayOfWeek,
    LocalTime openTime,
    LocalTime closeTime
) {}
