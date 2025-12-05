package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantPreparationSetting extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private Integer prepTimeMin;
    private Integer prepTimeMax;
    private Integer slotDuration;
    private Integer maxOrdersPerSlot;

    // ========== Calculate average prep time ==========
    public Integer calculateAveragePrepTime() {
        if (prepTimeMin == null || prepTimeMax == null) {
            return null;
        }
        return (prepTimeMin + prepTimeMax) / 2;
    }

    // ========== Validation ==========
    public void validate() {
        if (prepTimeMin != null && prepTimeMax != null && prepTimeMin > prepTimeMax) {
            throw new IllegalArgumentException("prepTimeMin must be <= prepTimeMax");
        }
        if (slotDuration != null && slotDuration <= 0) {
            throw new IllegalArgumentException("slotDuration must be > 0");
        }
        if (maxOrdersPerSlot != null && maxOrdersPerSlot <= 0) {
            throw new IllegalArgumentException("maxOrdersPerSlot must be > 0");
        }
    }
}
