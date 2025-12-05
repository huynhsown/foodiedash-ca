package com.ute.foodiedash.domain.restaurant.event;

public class RestaurantCreatedEvent {
    private final Long restaurantId;

    public RestaurantCreatedEvent(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }
}
