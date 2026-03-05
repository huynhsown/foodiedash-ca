package com.ute.foodiedash.interfaces.rest.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantPreparationSettingResponseDTO {

    private Long id;
    private Long restaurantId;
    private Integer prepTimeMin;
    private Integer prepTimeMax;
    private Integer slotDuration;
    private Integer maxOrdersPerSlot;
}
