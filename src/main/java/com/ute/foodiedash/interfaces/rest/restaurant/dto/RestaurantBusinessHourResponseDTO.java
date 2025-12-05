package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class RestaurantBusinessHourResponseDTO {

    private Long id;
    private Long restaurantId;
    private Integer dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
}
