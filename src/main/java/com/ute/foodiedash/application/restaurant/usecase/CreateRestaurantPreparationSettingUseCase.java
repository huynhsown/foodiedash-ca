package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantPreparationSettingCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantPreparationSettingQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.model.RestaurantPreparationSetting;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantPreparationSettingRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateRestaurantPreparationSettingUseCase {
    private final RestaurantPreparationSettingRepository restaurantPreparationSettingRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public RestaurantPreparationSettingQueryResult execute(CreateRestaurantPreparationSettingCommand command) {
        if (!restaurantRepository.existsById(command.restaurantId())) {
            throw new NotFoundException("Restaurant not found with id " + command.restaurantId());
        }

        RestaurantPreparationSetting setting = new RestaurantPreparationSetting();
        setting.setRestaurantId(command.restaurantId());
        setting.setPrepTimeMin(command.prepTimeMin());
        setting.setPrepTimeMax(command.prepTimeMax());
        setting.setSlotDuration(command.slotDuration());
        setting.setMaxOrdersPerSlot(command.maxOrdersPerSlot());
        setting.validate();

        RestaurantPreparationSetting saved = restaurantPreparationSettingRepository.save(setting);

        return new RestaurantPreparationSettingQueryResult(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getPrepTimeMin(),
            saved.getPrepTimeMax(),
            saved.getSlotDuration(),
            saved.getMaxOrdersPerSlot()
        );
    }
}
