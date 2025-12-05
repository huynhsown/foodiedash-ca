package com.ute.foodiedash.application.menu.query;

import com.ute.foodiedash.domain.menu.enums.MenuStatus;
import java.time.LocalTime;

public record MenuQueryResult(
    Long id,
    Long restaurantId,
    String name,
    LocalTime startTime,
    LocalTime endTime,
    MenuStatus status
) {}
