package com.ute.foodiedash.domain.menu.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemOption extends BaseEntity {
    private Long id;
    private Long menuItemId;
    private String name;
    private Boolean required = false;
    private Integer minValue;
    private Integer maxValue;

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
}
