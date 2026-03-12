package com.ute.foodiedash.domain.order.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class OrderItemOption extends BaseEntity {
    private Long id;
    private Long orderItemId;
    private Long optionId;
    private String optionName;
    private Boolean required;
    private Integer minValue;
    private Integer maxValue;

    private final List<OrderItemOptionValue> values = new ArrayList<>();

    public static OrderItemOption create(
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

        OrderItemOption option = new OrderItemOption();

        option.optionId = optionId;
        option.optionName = optionName;
        option.required = required != null ? required : Boolean.FALSE;
        option.minValue = minValue;
        option.maxValue = maxValue;

        return option;
    }

    public static OrderItemOption reconstruct(
            Long id,
            Long orderItemId,
            Long optionId,
            String optionName,
            Boolean required,
            Integer minValue,
            Integer maxValue,
            List<OrderItemOptionValue> values,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        OrderItemOption option = new OrderItemOption();

        option.id = id;
        option.orderItemId = orderItemId;
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

    public void addValue(OrderItemOptionValue value) {
        if (value == null) {
            throw new BadRequestException("Order item option value required");
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
                .map(OrderItemOptionValue::getQuantity)
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
