package com.ute.foodiedash.interfaces.rest.menu.dto;

import com.ute.foodiedash.domain.menu.enums.MenuStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class MenuResponseDTO {
    private Long id;
    private Long restaurantId;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private MenuStatus status;
}
