package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantPreparationSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SoftDeleteRestaurantPreparationSettingUseCase {
    private final RestaurantPreparationSettingRepository restaurantPreparationSettingRepository;

    @Transactional
    public void execute(Long id) {
        restaurantPreparationSettingRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant Preparation Setting not found"));
        restaurantPreparationSettingRepository.softDeleteById(id);
    }
}
