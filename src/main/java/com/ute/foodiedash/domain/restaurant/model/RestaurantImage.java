package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantImage extends BaseEntity {
    private Long id;
    private String imageUrl;
    private Long restaurantId;
}
