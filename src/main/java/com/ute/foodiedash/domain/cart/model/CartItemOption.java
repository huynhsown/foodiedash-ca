package com.ute.foodiedash.domain.cart.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class CartItemOption extends BaseEntity {
    private Long id;
    private Long cartItemId;
    private Long optionId;
    private String optionName;
    private Boolean required;
    private Integer minValue;
    private Integer maxValue;

    private final List<CartItemOptionValue> values = new ArrayList<>();

    public static CartItemOption create(
            Long optionId,
            String optionName,
            Boolean required,
            Integer minValue,
            Integer maxValue
    ) {
        if (optionId == null) {
            throw new BadRequestException("Option id required");
        }

        if (optionName == null || optionName.isBlank()) {
            throw new BadRequestException("Option name required");
        }

        if (minValue != null && minValue < 0) {
            throw new BadRequestException("Min value must be zero or positive");
        }

        if (maxValue != null && maxValue <= 0) {
            throw new BadRequestException("Max value must be positive");
        }

        if (minValue != null && maxValue != null && minValue > maxValue) {
            throw new BadRequestException("Min value cannot be greater than max value");
        }

        CartItemOption option = new CartItemOption();

        option.optionId = optionId;
        option.optionName = optionName;
        option.required = required != null ? required : Boolean.FALSE;
        option.minValue = minValue;
        option.maxValue = maxValue;

        return option;
    }

    public static CartItemOption reconstruct(
            Long id,
            Long cartItemId,
            Long optionId,
            String optionName,
            Boolean required,
            Integer minValue,
            Integer maxValue,
            List<CartItemOptionValue> values,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        CartItemOption option = new CartItemOption();

        option.id = id;
        option.cartItemId = cartItemId;
        option.optionId = optionId;
        option.optionName = optionName;
        option.required = required;
        option.minValue = minValue;
        option.maxValue = maxValue;

        if (values != null && !values.isEmpty()) {
            option.values.addAll(values);
        }

        option.restoreAudit(
                createdAt,
                updatedAt,
                createdBy,
                updatedBy,
                deletedAt,
                version
        );

        return option;
    }

    public void addValue(CartItemOptionValue value) {
        if (value == null) {
            throw new BadRequestException("Cart item option value required");
        }
        this.values.add(value);
    }

    public void removeValueById(Long valueId) {
        if (valueId == null) {
            return;
        }
        this.values.removeIf(v -> Objects.equals(v.getId(), valueId));
    }

    public void clearValues() {
        this.values.clear();
    }

    public int getTotalSelectedQuantity() {
        return this.values.stream()
                .map(CartItemOptionValue::getQuantity)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public void validateSelection() {
        int totalQuantity = getTotalSelectedQuantity();

        int effectiveMin = 0;
        if (Boolean.TRUE.equals(required)) {
            effectiveMin = minValue != null ? minValue : 1;
        } else if (minValue != null) {
            effectiveMin = minValue;
        }

        if (totalQuantity < effectiveMin) {
            throw new BadRequestException(
                    "Option \"%s\" requires at least %d value(s)".formatted(optionName, effectiveMin)
            );
        }

        if (maxValue != null && totalQuantity > maxValue) {
            throw new BadRequestException(
                    "Option \"%s\" allows at most %d value(s)".formatted(optionName, maxValue)
            );
        }
    }
}
