package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.repository.MenuItemOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SoftDeleteMenuItemOptionUseCase {
    private final MenuItemOptionRepository menuItemOptionRepository;

    @Transactional
    public void execute(Long id) {
        menuItemOptionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Menu Item Option not found"));
        menuItemOptionRepository.softDeleteById(id);
    }
}
