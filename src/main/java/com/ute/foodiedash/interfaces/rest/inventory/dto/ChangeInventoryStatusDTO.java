package com.ute.foodiedash.interfaces.rest.inventory.dto;

import com.ute.foodiedash.domain.inventory.enums.InventoryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeInventoryStatusDTO {

    @NotNull(message = "Status is required")
    private InventoryStatus status;
}
