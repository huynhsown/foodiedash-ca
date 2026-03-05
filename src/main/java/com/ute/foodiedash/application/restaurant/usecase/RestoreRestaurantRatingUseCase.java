package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.domain.restaurant.repository.RestaurantRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestoreRestaurantRatingUseCase {
    private final RestaurantRatingRepository restaurantRatingRepository;

    @Transactional
    public void execute(Long id) {
        restaurantRatingRepository.restoreById(id);
    }
}
