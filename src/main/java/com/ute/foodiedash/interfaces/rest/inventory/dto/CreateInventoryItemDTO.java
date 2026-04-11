package com.ute.foodiedash.interfaces.rest.inventory.dto;

import com.ute.foodiedash.domain.inventory.enums.InventoryUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateInventoryItemDTO {

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU must not exceed 100 characters")
    private String sku;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotNull(message = "Unit is required")
    private InventoryUnit unit;

    private BigDecimal reorderLevel;

    private BigDecimal reorderQuantity;

    @Positive(message = "Unit cost must be positive")
    private BigDecimal unitCost;
}
