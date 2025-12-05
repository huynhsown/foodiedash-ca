package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantRating extends BaseEntity {
    private Long id;
    private BigDecimal ratingAvg;
    private Long restaurantId;
    private Integer ratingCount;

    // ========== Null-safe rating as Double ==========
    public Double getRatingAvgAsDouble() {
        return ratingAvg != null ? ratingAvg.doubleValue() : null;
    }
}
