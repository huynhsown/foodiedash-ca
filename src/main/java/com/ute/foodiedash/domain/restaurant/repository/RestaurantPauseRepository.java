package com.ute.foodiedash.domain.restaurant.repository;

import com.ute.foodiedash.domain.restaurant.model.RestaurantPause;
import java.util.Optional;

public interface RestaurantPauseRepository {
    RestaurantPause save(RestaurantPause pause);
    Optional<RestaurantPause> findById(Long id);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
