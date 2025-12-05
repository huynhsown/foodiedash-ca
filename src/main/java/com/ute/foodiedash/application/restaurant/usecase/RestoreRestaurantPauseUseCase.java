package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.domain.restaurant.repository.RestaurantPauseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestoreRestaurantPauseUseCase {
    private final RestaurantPauseRepository restaurantPauseRepository;

    @Transactional
    public void execute(Long id) {
        restaurantPauseRepository.restoreById(id);
    }
}
