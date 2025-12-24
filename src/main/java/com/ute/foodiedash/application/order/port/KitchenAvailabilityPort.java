package com.ute.foodiedash.application.order.port;

public interface KitchenAvailabilityPort {
    boolean isKitchenAvailable(Long restaurantId);
}