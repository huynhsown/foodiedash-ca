package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRestaurantImageDTO {

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;
}
