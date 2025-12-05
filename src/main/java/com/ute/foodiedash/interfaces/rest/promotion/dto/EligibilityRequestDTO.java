package com.ute.foodiedash.interfaces.rest.promotion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EligibilityRequestDTO {
    @NotBlank
    private String promotionCode;

    @NotNull
    @Positive
    private Long userId;

    @NotNull
    @Positive
    private Long restaurantId;

    @NotNull
    @Positive
    private BigDecimal orderSubtotal;
}
