package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.command.CreateMenuItemOptionCommand;
import com.ute.foodiedash.application.menu.query.MenuItemOptionQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.model.MenuItemOption;
import com.ute.foodiedash.domain.menu.repository.MenuItemOptionRepository;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateMenuItemOptionUseCase {
    private final MenuItemOptionRepository menuItemOptionRepository;
    private final MenuItemRepository menuItemRepository;

    @Transactional
    public MenuItemOptionQueryResult execute(CreateMenuItemOptionCommand command) {
        if (!menuItemRepository.existsById(command.menuItemId())) {
            throw new NotFoundException("Menu Item not found with id " + command.menuItemId());
        }

        MenuItemOption option = new MenuItemOption();
        option.setMenuItemId(command.menuItemId());
        option.setName(command.name());
        option.setRequired(command.required() != null ? command.required() : false);
        option.setMinValue(command.minValue());
        option.setMaxValue(command.maxValue());

        MenuItemOption saved = menuItemOptionRepository.save(option);

        return new MenuItemOptionQueryResult(
            saved.getId(),
            saved.getMenuItemId(),
            saved.getName(),
            saved.getRequired(),
            saved.getMinValue(),
            saved.getMaxValue(),
            null
        );
    }
}
