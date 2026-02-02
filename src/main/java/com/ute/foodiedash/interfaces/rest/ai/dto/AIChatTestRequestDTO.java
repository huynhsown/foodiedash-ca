package com.ute.foodiedash.interfaces.rest.ai.dto;

import jakarta.validation.constraints.NotBlank;

public record AIChatTestRequestDTO(
        @NotBlank(message = "message must not be blank")
        String message
) {
}
