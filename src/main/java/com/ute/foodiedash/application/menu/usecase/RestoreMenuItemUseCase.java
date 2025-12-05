package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestoreMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public void execute(Long id) {
        menuItemRepository.restoreById(id);
    }
}
