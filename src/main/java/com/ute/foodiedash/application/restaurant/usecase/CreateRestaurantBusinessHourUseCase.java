package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantBusinessHourCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantBusinessHourQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.model.RestaurantBusinessHour;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantBusinessHourRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateRestaurantBusinessHourUseCase {
    private final RestaurantBusinessHourRepository restaurantBusinessHourRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public RestaurantBusinessHourQueryResult execute(CreateRestaurantBusinessHourCommand command) {
        if (!restaurantRepository.existsById(command.restaurantId())) {
            throw new NotFoundException("Restaurant not found with id " + command.restaurantId());
        }

        RestaurantBusinessHour businessHour = new RestaurantBusinessHour();
        businessHour.setRestaurantId(command.restaurantId());
        businessHour.setDayOfWeek(command.dayOfWeek());
        businessHour.setOpenTime(command.openTime());
        businessHour.setCloseTime(command.closeTime());
        businessHour.validate();

        RestaurantBusinessHour saved = restaurantBusinessHourRepository.save(businessHour);

        return new RestaurantBusinessHourQueryResult(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getDayOfWeek(),
            saved.getOpenTime(),
            saved.getCloseTime()
        );
    }
}
