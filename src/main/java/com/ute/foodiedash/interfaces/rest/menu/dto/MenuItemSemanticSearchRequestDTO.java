package com.ute.foodiedash.interfaces.rest.menu.dto;

import jakarta.validation.constraints.NotBlank;

public record MenuItemSemanticSearchRequestDTO(
        @NotBlank(message = "message is required")
        String message
) {
}
