package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    public boolean belongsToUser(Long userId) {
        return this.userId.equals(userId);
    }

    public boolean belongsToRestaurant(Long restaurantId) {
        return this.restaurantId.equals(restaurantId);
    }
}
