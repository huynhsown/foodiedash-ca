package com.ute.foodiedash.domain.order.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class OrderItemOptionValue extends BaseEntity {
    private Long id;
    private Long orderItemOptionId;
    private Long optionValueId;
    private String optionValueName;
    private Integer quantity;
    private BigDecimal extraPrice;

    public static OrderItemOptionValue create(
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

        OrderItemOptionValue value = new OrderItemOptionValue();

        value.optionValueId = optionValueId;
        value.optionValueName = optionValueName;
        value.quantity = quantity;
        value.extraPrice = extraPrice;

        return value;
    }

    public static OrderItemOptionValue reconstruct(
            Long id,
            Long orderItemOptionId,
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

        OrderItemOptionValue value = new OrderItemOptionValue();

        value.id = id;
        value.orderItemOptionId = orderItemOptionId;
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
