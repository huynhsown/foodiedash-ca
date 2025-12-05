package com.ute.foodiedash.application.menu.usecase;

import com.ute.foodiedash.application.menu.command.CreateMenuCommand;
import com.ute.foodiedash.application.menu.query.MenuQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.menu.model.Menu;
import com.ute.foodiedash.domain.menu.repository.MenuRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateMenuUseCase {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public MenuQueryResult execute(CreateMenuCommand command) {
        if (!restaurantRepository.existsById(command.restaurantId())) {
            throw new NotFoundException("Restaurant not found with id " + command.restaurantId());
        }

        Menu menu = Menu.create(
            command.restaurantId(), command.name(),
            command.startTime(), command.endTime()
        );

        Menu saved = menuRepository.save(menu);

        return new MenuQueryResult(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getName(),
            saved.getStartTime(),
            saved.getEndTime(),
            saved.getStatus()
        );
    }
}
