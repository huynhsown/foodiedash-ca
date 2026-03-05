package com.ute.foodiedash.domain.promotion.repository;

import com.ute.foodiedash.domain.promotion.model.PromotionRestaurant;

public interface PromotionRestaurantRepository {
    PromotionRestaurant save(PromotionRestaurant promotionRestaurant);
    boolean existsByPromotionIdAndRestaurantIdAndNotDeleted(Long promotionId, Long restaurantId);
}
