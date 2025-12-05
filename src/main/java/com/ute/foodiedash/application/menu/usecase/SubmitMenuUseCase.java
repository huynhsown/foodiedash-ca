package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.query.MenuQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.model.Menu;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import com.ute.foodiedash.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SubmitMenuUseCase {
    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public MenuQueryResult execute(Long id) {
        Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Menu not found with id " + id));

        boolean hasMenuItems = menuItemRepository.existsByMenuIdAndDeletedAtIsNull(id);
        if (!hasMenuItems) {
            throw new BadRequestException(
                "Cannot submit menu with id " + id + ". Menu must have at least one menu item.");
        }

        menu.submit();
        Menu savedMenu = menuRepository.save(menu);

        return new MenuQueryResult(
            savedMenu.getId(),
            savedMenu.getRestaurantId(),
            savedMenu.getName(),
            savedMenu.getStartTime(),
            savedMenu.getEndTime(),
            savedMenu.getStatus()
        );
    }
}
