package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestoreRestaurantCategoryMapUseCase {
    private final RestaurantCategoryMapRepository restaurantCategoryMapRepository;

    @Transactional
    public void execute(Long id) {
        restaurantCategoryMapRepository.restoreById(id);
    }
}
