package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.domain.restaurant.repository.RestaurantImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestoreRestaurantImageUseCase {
    private final RestaurantImageRepository restaurantImageRepository;

    @Transactional
    public void execute(Long id) {
        restaurantImageRepository.restoreById(id);
    }
}
