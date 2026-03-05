package com.ute.foodiedash.interfaces.rest.promotion.dto;

import com.ute.foodiedash.domain.promotion.enums.PromotionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePromotionStatusRequestDTO {
    @NotNull
    private PromotionStatus status;
}
