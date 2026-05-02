package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.port.MenuItemSearchIndexPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestMenuItemSearchIndexUseCase {

    private final MenuItemSearchIndexPort menuItemSearchIndexPort;

    public void index(Long menuItemId) {
        menuItemSearchIndexPort.indexMenuItem(menuItemId);
    }

    public void delete(Long menuItemId) {
        menuItemSearchIndexPort.deleteMenuItem(menuItemId);
    }
}
