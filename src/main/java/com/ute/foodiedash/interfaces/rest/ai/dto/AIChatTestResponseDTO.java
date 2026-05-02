package com.ute.foodiedash.interfaces.rest.ai.dto;

public record AIChatTestResponseDTO(
        String message,
        String type,
        Object data
) {
}
