package com.ute.foodiedash.domain.restaurant.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.restaurant.enums.RestaurantStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Restaurant extends BaseEntity {
    private Long id;
    private String code;
    private String slug;
    private String name;
    private String description;
    private String address;
    private String phone;
    private BigDecimal lat;
    private BigDecimal lng;
    private RestaurantStatus status;

    public static Restaurant create(String code, String name, String description,
                                    String address, String phone,
                                    BigDecimal lat, BigDecimal lng, String slug) {
        Restaurant restaurant = new Restaurant();
        restaurant.code = code;
        restaurant.name = name;
        restaurant.description = description;
        restaurant.address = address;
        restaurant.phone = phone;
        restaurant.lat = lat;
        restaurant.lng = lng;
        restaurant.slug = slug;
        restaurant.status = RestaurantStatus.ACTIVE;
        return restaurant;
    }

    public void ensureActive() {
        if (!isActive()) {
            throw new BadRequestException("Restaurant is not active");
        }
    }

    public void assignSlug(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException("Slug must not be blank");
        }
        this.slug = slug;
    }

    public boolean hasCoordinates() {
        return lat != null && lng != null;
    }

    public void updateSlug(String slug) {
        this.slug = slug;
    }

    public boolean isActive() {
        return status == RestaurantStatus.ACTIVE;
    }
}
