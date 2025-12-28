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

    public static RestaurantDevice reconstruct(
            Long id,
            Long userId,
            Long restaurantId,
            String deviceName,
            Instant lastLoginAt,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        RestaurantDevice device = new RestaurantDevice();
        device.id = id;
        device.userId = userId;
        device.restaurantId = restaurantId;
        device.deviceName = deviceName;
        device.lastLoginAt = lastLoginAt;
        device.restoreAudit(createdAt, updatedAt, createdBy, updatedBy, deletedAt, version);
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
