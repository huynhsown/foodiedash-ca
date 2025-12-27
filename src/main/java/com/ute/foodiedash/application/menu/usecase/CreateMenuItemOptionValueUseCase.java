package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.command.CreateMenuItemOptionValueCommand;
import com.ute.foodiedash.application.menu.query.MenuItemOptionValueQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateMenuItemOptionValueUseCase {
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public MenuItemOptionValueQueryResult execute(CreateMenuItemOptionValueCommand command) {
        MenuItem menuItem = menuItemRepository.findByOptionId(command.optionId())
                .orElseThrow(() -> new NotFoundException("Menu Item not found for option id " + command.optionId()));

        menuItem.addOptionValue(command.optionId(), command.name(), command.extraPrice());

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        MenuItemOptionValue savedValue = savedMenuItem.getOptionValue(command.optionId(), command.name())
                .orElseThrow(() -> new NotFoundException("Menu Item Option Value not found after save"));

        return new MenuItemOptionValueQueryResult(
                savedValue.getId(),
                savedValue.getOptionId(),
                savedValue.getName(),
                savedValue.getExtraPrice()
        );
    }
}
