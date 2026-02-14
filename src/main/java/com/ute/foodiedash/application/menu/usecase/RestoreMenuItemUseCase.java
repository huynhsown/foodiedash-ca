package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.port.MenuItemSearchIndexPort;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestoreMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemSearchIndexPort menuItemSearchIndexPort;

    @Transactional
    public void execute(Long id) {
        menuItemRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Menu Item not found"));
        menuItemRepository.restoreById(id);
        menuItemSearchIndexPort.indexMenuItem(id);
    }
}
