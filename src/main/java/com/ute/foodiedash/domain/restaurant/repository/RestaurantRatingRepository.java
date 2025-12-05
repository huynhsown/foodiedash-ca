package com.ute.foodiedash.domain.restaurant.repository;

import com.ute.foodiedash.domain.restaurant.model.RestaurantRating;
import java.util.List;
import java.util.Optional;

public interface RestaurantRatingRepository {
    RestaurantRating save(RestaurantRating restaurantRating);
    Optional<RestaurantRating> findById(Long id);
    List<RestaurantRating> findByRestaurantId(Long restaurantId, boolean includeDeleted);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
