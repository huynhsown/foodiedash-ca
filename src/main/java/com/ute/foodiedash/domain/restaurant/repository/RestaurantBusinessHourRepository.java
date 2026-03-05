package com.ute.foodiedash.domain.restaurant.repository;

import com.ute.foodiedash.domain.restaurant.model.RestaurantBusinessHour;
import java.util.List;
import java.util.Optional;

public interface RestaurantBusinessHourRepository {
    RestaurantBusinessHour save(RestaurantBusinessHour businessHour);
    Optional<RestaurantBusinessHour> findById(Long id);
    List<RestaurantBusinessHour> findByRestaurantId(Long restaurantId, boolean includeDeleted);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
