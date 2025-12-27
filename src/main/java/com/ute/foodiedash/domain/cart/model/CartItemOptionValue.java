package com.ute.foodiedash.domain.cart.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class CartItemOptionValue extends BaseEntity {
    private Long id;
    private Long cartItemOptionId;
    private Long optionValueId;
    private String optionValueName;
    private Integer quantity;
    private BigDecimal extraPrice;

    public static CartItemOptionValue create(
            Long optionValueId,
            String optionValueName,
            Integer quantity,
            BigDecimal extraPrice
    ) {
        if (optionValueId == null) {
            throw new BadRequestException("Option value id required");
        }

        if (optionValueName == null || optionValueName.isBlank()) {
            throw new BadRequestException("Option value name required");
        }

        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("Quantity must be positive");
        }

        if (extraPrice == null) {
            extraPrice = BigDecimal.ZERO;
        }

        if (extraPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Extra price must be positive");
        }

        CartItemOptionValue value = new CartItemOptionValue();

        value.optionValueId = optionValueId;
        value.optionValueName = optionValueName;
        value.quantity = quantity;
        value.extraPrice = extraPrice;

        return value;
    }

    public static CartItemOptionValue reconstruct(
            Long id,
            Long cartItemOptionId,
            Long optionValueId,
            String optionValueName,
            Integer quantity,
            BigDecimal extraPrice,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        CartItemOptionValue value = new CartItemOptionValue();

        value.id = id;
        value.cartItemOptionId = cartItemOptionId;
        value.optionValueId = optionValueId;
        value.optionValueName = optionValueName;
        value.quantity = quantity;
        value.extraPrice = extraPrice;

        value.restoreAudit(
                createdAt,
                updatedAt,
                createdBy,
                updatedBy,
                deletedAt,
                version
        );

        return value;
    }

    public BigDecimal getTotalExtraPrice() {
        return extraPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
