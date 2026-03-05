package com.ute.foodiedash.interfaces.rest.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMenuItemOptionDTO {

    @NotNull(message = "Menu Item ID is required")
    private Long menuItemId;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    private Boolean required = false;

    private Integer minValue;

    private Integer maxValue;
}
