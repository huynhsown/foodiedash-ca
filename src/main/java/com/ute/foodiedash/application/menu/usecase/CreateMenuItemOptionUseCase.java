package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.command.CreateMenuItemOptionCommand;
import com.ute.foodiedash.application.menu.query.MenuItemOptionQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.model.MenuItemOption;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateMenuItemOptionUseCase {
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public MenuItemOptionQueryResult execute(CreateMenuItemOptionCommand command) {
        MenuItem menuItem = menuItemRepository.findById(command.menuItemId())
            .orElseThrow(() -> new NotFoundException("Menu Item not found with id " + command.menuItemId()));

        MenuItemOption option = menuItem.addOption(
            command.name(),
            command.required(),
            command.minValue(),
            command.maxValue()
        );

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        MenuItemOption savedOption = savedMenuItem.getOptions().stream()
            .filter(o -> o.getName().equals(option.getName()))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Created menu item option not found after save"));

        return new MenuItemOptionQueryResult(
            savedOption.getId(),
            savedOption.getMenuItemId(),
            savedOption.getName(),
            savedOption.getRequired(),
            savedOption.getMinValue(),
            savedOption.getMaxValue(),
            null
        );
    }
}
