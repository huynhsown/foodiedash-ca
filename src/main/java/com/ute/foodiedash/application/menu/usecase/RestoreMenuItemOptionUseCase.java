package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.domain.menu.repository.MenuItemOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestoreMenuItemOptionUseCase {
    private final MenuItemOptionRepository menuItemOptionRepository;

    @Transactional
    public void execute(Long id) {
        menuItemOptionRepository.restoreById(id);
    }
}
