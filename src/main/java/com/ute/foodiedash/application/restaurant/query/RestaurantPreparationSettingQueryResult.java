package com.ute.foodiedash.application.restaurant.query;

public record RestaurantPreparationSettingQueryResult(
    Long id,
    Long restaurantId,
    Integer prepTimeMin,
    Integer prepTimeMax,
    Integer slotDuration,
    Integer maxOrdersPerSlot
) {}
