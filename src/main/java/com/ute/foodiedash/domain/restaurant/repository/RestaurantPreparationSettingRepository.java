package com.ute.foodiedash.domain.restaurant.repository;

import com.ute.foodiedash.domain.restaurant.model.RestaurantPreparationSetting;
import java.util.List;
import java.util.Optional;

public interface RestaurantPreparationSettingRepository {
    RestaurantPreparationSetting save(RestaurantPreparationSetting setting);
    Optional<RestaurantPreparationSetting> findById(Long id);
    List<RestaurantPreparationSetting> findByRestaurantId(Long restaurantId, boolean includeDeleted);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
