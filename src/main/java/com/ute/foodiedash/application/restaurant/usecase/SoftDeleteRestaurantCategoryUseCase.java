package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SoftDeleteRestaurantCategoryUseCase {
    private final RestaurantCategoryRepository restaurantCategoryRepository;

    @Transactional
    public void execute(Long id) {
        restaurantCategoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant Category not found"));
        restaurantCategoryRepository.softDeleteById(id);
    }
}
