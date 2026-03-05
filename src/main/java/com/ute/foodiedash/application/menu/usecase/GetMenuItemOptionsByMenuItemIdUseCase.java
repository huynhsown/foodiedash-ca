package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.query.MenuItemOptionQueryResult;
import com.ute.foodiedash.application.menu.query.MenuItemOptionValueQueryResult;
import com.ute.foodiedash.domain.menu.model.MenuItemOption;
import com.ute.foodiedash.domain.menu.model.MenuItemOptionValue;
import com.ute.foodiedash.domain.menu.repository.MenuItemOptionRepository;
import com.ute.foodiedash.domain.menu.repository.MenuItemOptionValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetMenuItemOptionsByMenuItemIdUseCase {
    private final MenuItemOptionRepository menuItemOptionRepository;
    private final MenuItemOptionValueRepository menuItemOptionValueRepository;

    public List<MenuItemOptionQueryResult> execute(Long menuItemId) {
        List<MenuItemOption> options = menuItemOptionRepository
            .findByMenuItemIdAndDeletedAtIsNull(menuItemId);
        
        List<Long> optionIds = options.stream()
            .map(MenuItemOption::getId)
            .toList();
        
        List<MenuItemOptionValue> optionValues = menuItemOptionValueRepository
            .findByOptionIdInAndDeletedAtIsNull(optionIds);
        
        Map<Long, List<MenuItemOptionValue>> optionMap = optionValues.stream()
            .collect(Collectors.groupingBy(MenuItemOptionValue::getOptionId));
        
        List<MenuItemOptionQueryResult> results = new ArrayList<>();
        
        for (MenuItemOption option : options) {
            List<MenuItemOptionValue> values = optionMap.getOrDefault(option.getId(), Collections.emptyList());
            
            List<MenuItemOptionValueQueryResult> valueResults = values.stream()
                .map(value -> new MenuItemOptionValueQueryResult(
                    value.getId(),
                    value.getOptionId(),
                    value.getName(),
                    value.getExtraPrice()
                ))
                .collect(Collectors.toList());
            
            results.add(new MenuItemOptionQueryResult(
                option.getId(),
                option.getMenuItemId(),
                option.getName(),
                option.getRequired(),
                option.getMinValue(),
                option.getMaxValue(),
                valueResults
            ));
        }
        
        return results;
    }
}
