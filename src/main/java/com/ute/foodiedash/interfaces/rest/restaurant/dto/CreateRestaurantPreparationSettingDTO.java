package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRestaurantPreparationSettingDTO {

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotNull(message = "Preparation time min is required")
    @Positive(message = "Preparation time min must be positive")
    private Integer prepTimeMin;

    @NotNull(message = "Preparation time max is required")
    @Positive(message = "Preparation time max must be positive")
    private Integer prepTimeMax;

    @NotNull(message = "Slot duration is required")
    @Positive(message = "Slot duration must be positive")
    private Integer slotDuration;

    @NotNull(message = "Max orders per slot is required")
    @Positive(message = "Max orders per slot must be positive")
    private Integer maxOrdersPerSlot;
}
