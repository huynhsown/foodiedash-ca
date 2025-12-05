package com.ute.foodiedash.domain.menu.repository;

import com.ute.foodiedash.domain.menu.model.Menu;
import java.util.List;
import java.util.Optional;

public interface MenuRepository {
    Menu save(Menu menu);
    Optional<Menu> findById(Long id);
    List<Menu> findByRestaurantId(Long restaurantId);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
