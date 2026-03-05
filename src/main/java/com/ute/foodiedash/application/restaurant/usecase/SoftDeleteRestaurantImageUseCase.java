package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SoftDeleteRestaurantImageUseCase {
    private final RestaurantImageRepository restaurantImageRepository;

    @Transactional
    public void execute(Long id) {
        restaurantImageRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant Image not found"));
        restaurantImageRepository.softDeleteById(id);
    }
}
