package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.domain.restaurant.repository.RestaurantBusinessHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestoreRestaurantBusinessHourUseCase {
    private final RestaurantBusinessHourRepository restaurantBusinessHourRepository;

    @Transactional
    public void execute(Long id) {
        restaurantBusinessHourRepository.restoreById(id);
    }
}
