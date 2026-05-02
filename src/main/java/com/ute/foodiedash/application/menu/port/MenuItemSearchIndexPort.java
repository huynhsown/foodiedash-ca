package com.ute.foodiedash.application.menu.port;

public interface MenuItemSearchIndexPort {
    void indexMenuItem(Long menuItemId);
    void deleteMenuItem(Long menuItemId);
}
