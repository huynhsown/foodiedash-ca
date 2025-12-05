package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantCategoryMap extends BaseEntity {
    private Long id;
    private Long restaurantId;
    private Long categoryId;
}
