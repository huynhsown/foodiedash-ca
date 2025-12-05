package com.ute.foodiedash.interfaces.rest.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class MenuDetailDTO {
    private Long id;
    private String name;
    
    @JsonProperty("is_available")
    private Boolean isAvailable;
    
    @JsonProperty("start_time")
    private LocalTime startTime;
    
    @JsonProperty("end_time")
    private LocalTime endTime;
    
    private List<MenuItemResponseDTO> menuItems;
}
