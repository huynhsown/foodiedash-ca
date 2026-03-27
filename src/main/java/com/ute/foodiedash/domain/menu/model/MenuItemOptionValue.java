package com.ute.foodiedash.domain.menu.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;

@Getter
public class MenuItemOptionValue extends BaseEntity {
    private Long id;
    private Long optionId;
    private String name;
    private BigDecimal extraPrice;

    public static MenuItemOptionValue create(
            Long optionId,
            String name,
            BigDecimal extraPrice
    ) {
        if (optionId == null) {
            throw new BadRequestException("Option id required");
        }
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Option value name required");
        }
        if (extraPrice == null) {
            extraPrice = BigDecimal.ZERO;
        }
        if (extraPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Extra price must be zero or positive");
        }

        MenuItemOptionValue value = new MenuItemOptionValue();
        value.optionId = optionId;
        value.name = name;
        value.extraPrice = extraPrice;
        return value;
    }

    public static MenuItemOptionValue reconstruct(
            Long id,
            Long optionId,
            String name,
            BigDecimal extraPrice,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        MenuItemOptionValue value = new MenuItemOptionValue();

        value.id = id;
        value.optionId = optionId;
        value.name = name;
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
}
