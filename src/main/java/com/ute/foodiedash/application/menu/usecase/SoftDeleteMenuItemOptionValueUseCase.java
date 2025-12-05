package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.repository.MenuItemOptionValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SoftDeleteMenuItemOptionValueUseCase {
    private final MenuItemOptionValueRepository menuItemOptionValueRepository;

    @Transactional
    public void execute(Long id) {
        menuItemOptionValueRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Menu Item Option Value not found"));
        menuItemOptionValueRepository.softDeleteById(id);
    }
}
