package com.ute.foodiedash.domain.menu.repository;

import com.ute.foodiedash.domain.menu.model.MenuItemOption;
import java.util.List;
import java.util.Optional;

public interface MenuItemOptionRepository {
    MenuItemOption save(MenuItemOption option);
    Optional<MenuItemOption> findById(Long id);
    boolean existsById(Long id);
    List<MenuItemOption> findByMenuItemIdAndDeletedAtIsNull(Long menuItemId);
    void softDeleteById(Long id);
    void restoreById(Long id);
}
