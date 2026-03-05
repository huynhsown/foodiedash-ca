package com.ute.foodiedash.domain.promotion.model;

import com.ute.foodiedash.domain.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionRestaurant extends BaseEntity {
    private Long id;
    private Long promotionId;
    private Long restaurantId;

    public static PromotionRestaurant create(Long promotionId, Long restaurantId) {
        PromotionRestaurant pr = new PromotionRestaurant();
        pr.promotionId = promotionId;
        pr.restaurantId = restaurantId;
        return pr;
    }
}
