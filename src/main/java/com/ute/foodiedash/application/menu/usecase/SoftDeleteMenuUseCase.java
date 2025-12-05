package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SoftDeleteMenuUseCase {
    private final MenuRepository menuRepository;

    @Transactional
    public void execute(Long id) {
        menuRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Menu not found"));
        menuRepository.softDeleteById(id);
    }
}
