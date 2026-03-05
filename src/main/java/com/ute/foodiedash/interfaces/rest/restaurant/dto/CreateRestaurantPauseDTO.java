package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CreateRestaurantPauseDTO {

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotBlank(message = "Reason is required")
    @Size(max = 255, message = "Reason must not exceed 255 characters")
    private String reason;

    @NotNull(message = "Paused from is required")
    private Instant pausedFrom;

    @NotNull(message = "Paused to is required")
    @Future(message = "Paused to must be in the future")
    private Instant pausedTo;
}
