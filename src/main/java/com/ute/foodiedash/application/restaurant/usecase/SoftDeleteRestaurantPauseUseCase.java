package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantPauseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SoftDeleteRestaurantPauseUseCase {
    private final RestaurantPauseRepository restaurantPauseRepository;

    @Transactional
    public void execute(Long id) {
        restaurantPauseRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant Pause not found"));
        restaurantPauseRepository.softDeleteById(id);
    }
}
