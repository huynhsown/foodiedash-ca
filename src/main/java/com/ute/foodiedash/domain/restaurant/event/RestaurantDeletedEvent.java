package com.ute.foodiedash.domain.restaurant.event;

public class RestaurantDeletedEvent {
    private final Long restaurantId;

    public RestaurantDeletedEvent(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }
}
