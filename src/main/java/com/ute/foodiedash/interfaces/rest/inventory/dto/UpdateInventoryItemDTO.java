package com.ute.foodiedash.interfaces.rest.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateInventoryItemDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Positive(message = "Unit cost must be positive")
    private BigDecimal unitCost;

    private BigDecimal reorderLevel;

    private BigDecimal reorderQuantity;
}
