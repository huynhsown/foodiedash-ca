package com.ute.foodiedash.interfaces.rest.promotion.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ReservePromotionRequestDTO {
    @NotNull
    @Positive
    private Long promotionId;

    @NotNull
    @Positive
    private Long userId;

    @NotNull
    @Positive
    private Long orderId;
}
