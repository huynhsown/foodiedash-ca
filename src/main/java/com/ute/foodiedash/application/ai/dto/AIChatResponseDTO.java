package com.ute.foodiedash.application.ai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AIChatResponseDTO {
    private String message;
    private String type;
    private Object data;
}
