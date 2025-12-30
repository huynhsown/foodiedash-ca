package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;

import java.time.Instant;

@Getter
public class MerchantRestaurant extends BaseEntity {

    private Long id;
    private Long userId;
    private Long restaurantId;

    public static MerchantRestaurant create(Long userId, Long restaurantId) {
        MerchantRestaurant mr = new MerchantRestaurant();
        mr.userId = userId;
        mr.restaurantId = restaurantId;
        return mr;
    }

    public static MerchantRestaurant reconstruct(
            Long id,
            Long userId,
            Long restaurantId,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        MerchantRestaurant mr = new MerchantRestaurant();
        mr.id = id;
        mr.userId = userId;
        mr.restaurantId = restaurantId;
        mr.restoreAudit(createdAt, updatedAt, createdBy, updatedBy, deletedAt, version);
        return mr;
    }

    public boolean belongsToUser(Long userId) {
        return this.userId.equals(userId);
    }

    public boolean belongsToRestaurant(Long restaurantId) {
        return this.restaurantId.equals(restaurantId);
    }
}
