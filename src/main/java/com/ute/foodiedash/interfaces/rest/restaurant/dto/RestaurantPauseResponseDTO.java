package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class RestaurantPauseResponseDTO {

    private Long id;
    private Long restaurantId;
    private String reason;
    private Instant pausedFrom;
    private Instant pausedTo;
}
