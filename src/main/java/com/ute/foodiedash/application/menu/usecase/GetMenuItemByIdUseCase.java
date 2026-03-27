package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.query.MenuItemOptionQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemOptionValueQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetMenuItemByIdUseCase {
    private final MenuItemRepository menuItemRepository;

    public MenuItemQueryResult execute(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Menu Item not found"));

        List<MenuItemOptionQueryResult> optionResults = menuItem.getOptions().stream()
            .filter(option -> !option.isDeleted())
            .map(option -> {
                List<MenuItemOptionValueQueryResult> valueResults = option.getValues().stream()
                    .filter(value -> !value.isDeleted())
                    .map(value -> new MenuItemOptionValueQueryResult(
                        value.getId(),
                        value.getOptionId(),
                        value.getName(),
                        value.getExtraPrice()
                    ))
                    .collect(Collectors.toList());
                
                return new MenuItemOptionQueryResult(
                    option.getId(),
                    option.getMenuItemId(),
                    option.getName(),
                    option.getRequired(),
                    option.getMinValue(),
                    option.getMaxValue(),
                    valueResults
                );
            })
            .collect(Collectors.toList());

        return new MenuItemQueryResult(
            menuItem.getId(),
            menuItem.getMenuId(),
            menuItem.getRestaurantId(),
            menuItem.getName(),
            menuItem.getDescription(),
            menuItem.getPrice(),
            menuItem.getImageUrl(),
            menuItem.getStatus(),
            optionResults
        );
    }
}
