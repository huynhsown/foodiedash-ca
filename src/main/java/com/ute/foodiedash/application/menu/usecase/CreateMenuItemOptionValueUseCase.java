package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.command.CreateMenuItemOptionValueCommand;
import com.ute.foodiedash.application.menu.query.MenuItemOptionValueQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.domain.menu.repository.MenuItemOptionRepository;
import com.ute.foodiedash.domain.menu.repository.MenuItemOptionValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateMenuItemOptionValueUseCase {
    private final MenuItemOptionValueRepository menuItemOptionValueRepository;
    private final MenuItemOptionRepository menuItemOptionRepository;

    @Transactional
    public MenuItemOptionValueQueryResult execute(CreateMenuItemOptionValueCommand command) {
        if (!menuItemOptionRepository.existsById(command.optionId())) {
            throw new NotFoundException("Menu Item Option not found with id " + command.optionId());
        }

        MenuItemOptionValue optionValue = new MenuItemOptionValue();
        optionValue.setOptionId(command.optionId());
        optionValue.setName(command.name());
        optionValue.setExtraPrice(command.extraPrice());

        MenuItemOptionValue saved = menuItemOptionValueRepository.save(optionValue);

        return new MenuItemOptionValueQueryResult(
            saved.getId(),
            saved.getOptionId(),
            saved.getName(),
            saved.getExtraPrice()
        );
    }
}
