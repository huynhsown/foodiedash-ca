package com.ute.foodiedash.interfaces.rest.inventory.dto;

import com.ute.foodiedash.domain.inventory.enums.InventoryStatus;
import com.ute.foodiedash.domain.inventory.enums.InventoryUnit;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InventoryItemResponseDTO {
    private Long id;
    private Long restaurantId;
    private String sku;
    private String name;
    private InventoryUnit unit;
    private BigDecimal quantityOnHand;
    private BigDecimal reorderLevel;
    private BigDecimal reorderQuantity;
    private BigDecimal unitCost;
    private InventoryStatus status;
    private boolean lowStock;
}
