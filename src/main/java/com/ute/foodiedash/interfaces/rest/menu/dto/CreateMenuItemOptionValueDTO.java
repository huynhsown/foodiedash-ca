package com.ute.foodiedash.interfaces.rest.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateMenuItemOptionValueDTO {

    @NotNull(message = "Option ID is required")
    private Long optionId;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    private BigDecimal extraPrice;
}
