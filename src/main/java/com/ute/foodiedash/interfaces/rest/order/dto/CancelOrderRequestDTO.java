package com.ute.foodiedash.interfaces.rest.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelOrderRequestDTO {
    @NotBlank(message = "Reason is required")
    private String reason;
}

