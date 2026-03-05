package com.ute.foodiedash.application.menu.command;

import java.time.LocalTime;

public record CreateMenuCommand(
    Long restaurantId,
    String name,
    LocalTime startTime,
    LocalTime endTime
) {}
