package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.query.MenuDetailQueryResult;
import com.ute.foodiedash.application.menu.query.RestaurantMenuQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.model.Menu;
import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import com.ute.foodiedash.domain.menu.repository.MenuRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantBusinessHourRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.infrastructure.util.RestaurantUtils;
import com.ute.foodiedash.domain.menu.enums.MenuStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetRestaurantMenuUseCase {
    private final MenuRepository menuRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantBusinessHourRepository restaurantBusinessHourRepository;
    private final RestaurantUtils restaurantUtils;

    public RestaurantMenuQueryResult execute(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new NotFoundException("Restaurant not found");
        }

        boolean restaurantIsOpen = restaurantUtils.checkIfRestaurantIsOpen(restaurantId);

        List<Menu> menus = menuRepository.findByRestaurantId(restaurantId)
            .stream()
            .filter(menu -> menu.getStatus() == MenuStatus.ACTIVE)
            .toList();

        List<Long> menuIds = menus.stream()
            .map(Menu::getId)
            .toList();

        Map<Long, List<MenuItem>> menuMap = menuItemRepository
            .findByMenuIdInAndDeletedAtIsNull(menuIds)
            .stream()
            .collect(Collectors.groupingBy(MenuItem::getMenuId));

        List<MenuDetailQueryResult> menuDetails = new ArrayList<>();

        for (Menu menu : menus) {
            if (!menu.getStatus().equals(MenuStatus.ACTIVE)) continue;
            
            boolean isMenuAvailableNow = isMenuAvailableNow(menu);
            boolean isAvailable = restaurantIsOpen && isMenuAvailableNow;

            List<MenuItem> items = menuMap.getOrDefault(menu.getId(), Collections.emptyList());
            
            MenuDetailQueryResult menuDetail = new MenuDetailQueryResult(
                menu.getId(),
                menu.getName(),
                menu.getStartTime(),
                menu.getEndTime(),
                isAvailable,
                items.stream()
                    .map(item -> new com.ute.foodiedash.application.menu.query.MenuItemQueryResult(
                        item.getId(),
                        item.getMenuId(),
                        item.getRestaurantId(),
                        item.getName(),
                        item.getDescription(),
                        item.getPrice(),
                        item.getImageUrl(),
                        item.getStatus(),
                        null
                    ))
                    .toList()
            );
            
            menuDetails.add(menuDetail);
        }

        return new RestaurantMenuQueryResult(menuDetails);
    }

    private boolean isMenuAvailableNow(Menu menu) {
        LocalTime now = LocalTime.now();
        LocalTime start = menu.getStartTime();
        LocalTime end = menu.getEndTime();

        if (start.isBefore(end)) {
            return !now.isBefore(start) && !now.isAfter(end);
        }

        return !now.isBefore(start) || !now.isAfter(end);
    }
}
