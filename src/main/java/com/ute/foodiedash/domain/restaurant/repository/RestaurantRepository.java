package com.ute.foodiedash.domain.restaurant.repository;

import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import java.util.Optional;

public interface RestaurantRepository {
    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findById(Long id);
    Optional<Restaurant> findByIdAndDeletedAtIsNull(Long id);
    Optional<Restaurant> findBySlug(String slug);
    boolean existsById(Long id);
    boolean existsBySlug(String slug);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
