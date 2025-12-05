package com.ute.foodiedash.application.restaurant.command;

public record CreateRestaurantPreparationSettingCommand(
    Long restaurantId,
    Integer prepTimeMin,
    Integer prepTimeMax,
    Integer slotDuration,
    Integer maxOrdersPerSlot
) {}
