package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;

import java.time.Instant;

@Getter
public class RestaurantDevice extends BaseEntity {

    private Long id;
    private Long userId;
    private Long restaurantId;
    private String deviceName;
    private Instant lastLoginAt;

    public static RestaurantDevice create(Long userId, Long restaurantId, String deviceName) {
        RestaurantDevice device = new RestaurantDevice();
        device.userId = userId;
        device.restaurantId = restaurantId;
        device.deviceName = deviceName;
        return device;
    }

    public void recordLogin() {
        this.lastLoginAt = Instant.now();
    }

    public boolean belongsToRestaurant(Long restaurantId) {
        return this.restaurantId.equals(restaurantId);
    }

    public boolean belongsToUser(Long userId) {
        return this.userId.equals(userId);
    }
}
