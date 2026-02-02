package com.ute.foodiedash.interfaces.rest.menu.dto;

import java.util.List;

public record MenuItemSemanticSearchResponseDTO(
        List<ItemMatchDTO> matches
) {
    public record ItemMatchDTO(
            Long menuItemId,
            Double score
    ) {}
}
