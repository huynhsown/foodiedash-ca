package com.ute.foodiedash.domain.menu.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class MenuItemOption extends BaseEntity {
    private Long id;
    private Long menuItemId;
    private String name;
    private Boolean required = false;
    private Integer minValue;
    private Integer maxValue;

    private final List<MenuItemOptionValue> values = new ArrayList<>();

    public static MenuItemOption create(
            Long menuItemId,
            String name,
            Boolean required,
            Integer minValue,
            Integer maxValue
    ) {
        if (menuItemId == null) {
            throw new BadRequestException("Menu item id required");
        }
        if (name == null || name.isBlank()) {
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

        MenuItemOption option = new MenuItemOption();

        option.menuItemId = menuItemId;
        option.name = name;
        option.required = required != null ? required : Boolean.FALSE;
        option.minValue = minValue;
        option.maxValue = maxValue;

        return option;
    }

    public static MenuItemOption reconstruct(
            Long id,
            Long menuItemId,
            String name,
            Boolean required,
            Integer minValue,
            Integer maxValue,
            List<MenuItemOptionValue> values,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        MenuItemOption option = new MenuItemOption();

        option.id = id;
        option.menuItemId = menuItemId;
        option.name = name;
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

    // ========== Validate selection count ==========
    /**
     * Validate số lượng option values đã chọn dựa trên rules:
     * - Nếu required = true → phải chọn ít nhất 1
     * - Nếu có minValue → số lượng chọn >= minValue
     * - Nếu có maxValue → số lượng chọn <= maxValue
     */
    public void validateSelectionCount(int selectedCount) {
        if (Boolean.TRUE.equals(required) && selectedCount == 0) {
            throw new BadRequestException("Option is required: " + name);
        }
        if (minValue != null && selectedCount < minValue) {
            throw new BadRequestException("Option min value violated: " + name);
        }
        if (maxValue != null && selectedCount > maxValue) {
            throw new BadRequestException("Option max value violated: " + name);
        }
    }

    // ========== Check required ==========
    public boolean isRequired() {
        return Boolean.TRUE.equals(required);
    }

    public MenuItemOptionValue addValue(String name, BigDecimal extraPrice) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Option value name required");
        }

        boolean exists = this.values.stream()
                .anyMatch(v -> v.getName() != null && v.getName().equalsIgnoreCase(name));

        if (exists) {
            throw new BadRequestException("Option value name already exists: " + name);
        }

        MenuItemOptionValue value = MenuItemOptionValue.create(
                this.id,
                name,
                extraPrice
        );
        this.values.add(value);
        return value;
    }
}
