package com.ute.foodiedash.domain.menu.repository;

import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface MenuItemOptionValueRepository {
    MenuItemOptionValue save(MenuItemOptionValue optionValue);
    Optional<MenuItemOptionValue> findById(Long id);
    List<MenuItemOptionValue> findByOptionIdAndDeletedAt(Long optionId, Instant deletedAt);
    List<MenuItemOptionValue> findByOptionIdInAndDeletedAtIsNull(List<Long> optionIds);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
