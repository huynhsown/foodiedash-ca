package com.ute.foodiedash.domain.restaurant.event;

public class RestaurantUpdatedEvent {
    private final Long restaurantId;

    public RestaurantUpdatedEvent(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }
}
