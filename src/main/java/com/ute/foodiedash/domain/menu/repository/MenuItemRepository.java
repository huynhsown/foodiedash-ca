package com.ute.foodiedash.domain.menu.repository;

import com.ute.foodiedash.domain.menu.model.MenuItem;
import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {
    MenuItem save(MenuItem menuItem);
    Optional<MenuItem> findById(Long id);
    Optional<MenuItem> findByOptionId(Long optionId);
    boolean existsById(Long id);
    List<MenuItem> findByMenuIdInAndDeletedAtIsNull(List<Long> menuIds);
    List<MenuItem> findByMenuId(Long menuId, boolean includeDeleted);
    List<MenuItem> findByRestaurantId(Long restaurantId, boolean includeDeleted);
    boolean existsByMenuIdAndDeletedAtIsNull(Long menuId);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
