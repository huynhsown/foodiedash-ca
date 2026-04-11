package com.ute.foodiedash.interfaces.rest.inventory.dto;

import com.ute.foodiedash.domain.inventory.enums.InventoryStatus;
import com.ute.foodiedash.interfaces.rest.common.dto.PageRequestDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchInventoryItemsRequestDTO extends PageRequestDTO {
    private Long restaurantId;
    private Long id;
    private String keyword;
    private InventoryStatus status;
    private Boolean lowStock;
}
