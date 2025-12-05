package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantPauseCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantPauseQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.model.RestaurantPause;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantPauseRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateRestaurantPauseUseCase {
    private final RestaurantPauseRepository restaurantPauseRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public RestaurantPauseQueryResult execute(CreateRestaurantPauseCommand command) {
        if (!restaurantRepository.existsById(command.restaurantId())) {
            throw new NotFoundException("Restaurant not found with id " + command.restaurantId());
        }

        RestaurantPause pause = new RestaurantPause();
        pause.setRestaurantId(command.restaurantId());
        pause.setReason(command.reason());
        pause.setPausedFrom(command.pausedFrom());
        pause.setPausedTo(command.pausedTo());
        pause.validate();

        RestaurantPause saved = restaurantPauseRepository.save(pause);

        return new RestaurantPauseQueryResult(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getReason(),
            saved.getPausedFrom(),
            saved.getPausedTo()
        );
    }
}
