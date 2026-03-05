package com.ute.foodiedash.domain.restaurant.repository;

import com.ute.foodiedash.domain.restaurant.model.RestaurantCategoryMap;
import java.util.List;
import java.util.Optional;

public interface RestaurantCategoryMapRepository {
    RestaurantCategoryMap save(RestaurantCategoryMap categoryMap);
    Optional<RestaurantCategoryMap> findById(Long id);
    List<RestaurantCategoryMap> findByRestaurantId(Long restaurantId, boolean includeDeleted);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
