package com.ute.foodiedash.domain.restaurant.repository;

import com.ute.foodiedash.domain.restaurant.model.RestaurantImage;
import java.util.Optional;

public interface RestaurantImageRepository {
    RestaurantImage save(RestaurantImage image);
    Optional<RestaurantImage> findById(Long id);
    Optional<RestaurantImage> findFirstByRestaurantId(Long restaurantId);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
