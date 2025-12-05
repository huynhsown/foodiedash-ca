package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.domain.menu.repository.MenuItemOptionValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestoreMenuItemOptionValueUseCase {
    private final MenuItemOptionValueRepository menuItemOptionValueRepository;

    @Transactional
    public void execute(Long id) {
        menuItemOptionValueRepository.restoreById(id);
    }
}
