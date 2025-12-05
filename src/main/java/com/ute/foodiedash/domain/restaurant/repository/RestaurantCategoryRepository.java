package com.ute.foodiedash.domain.restaurant.repository;

import com.ute.foodiedash.domain.restaurant.model.RestaurantCategory;
import java.util.List;
import java.util.Optional;

public interface RestaurantCategoryRepository {
    RestaurantCategory save(RestaurantCategory category);
    Optional<RestaurantCategory> findById(Long id);
    boolean existsById(Long id);
    List<RestaurantCategory> findAll();
    List<RestaurantCategory> findByIdIn(List<Long> categoryIds);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
