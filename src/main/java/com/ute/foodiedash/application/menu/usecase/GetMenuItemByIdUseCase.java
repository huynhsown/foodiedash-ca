package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.query.MenuItemOptionQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemOptionValueQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.model.MenuItemOption;
import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.domain.menu.repository.MenuItemOptionRepository;
import com.ute.foodiedash.domain.menu.repository.MenuItemOptionValueRepository;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetMenuItemByIdUseCase {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemOptionRepository menuItemOptionRepository;
    private final MenuItemOptionValueRepository menuItemOptionValueRepository;

    public MenuItemQueryResult execute(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Menu Item not found"));

        List<MenuItemOption> options = menuItemOptionRepository.findByMenuItemIdAndDeletedAtIsNull(id);
        
        List<MenuItemOptionQueryResult> optionResults = options.stream()
            .map(option -> {
                List<MenuItemOptionValue> values = menuItemOptionValueRepository
                    .findByOptionIdAndDeletedAt(option.getId(), null);
                
                List<MenuItemOptionValueQueryResult> valueResults = values.stream()
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
