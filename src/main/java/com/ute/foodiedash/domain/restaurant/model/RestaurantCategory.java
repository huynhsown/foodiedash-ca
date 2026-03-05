package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantCategory extends BaseEntity {
    private Long id;
    private String name;
    private String iconUrl;
    private String description;
}
