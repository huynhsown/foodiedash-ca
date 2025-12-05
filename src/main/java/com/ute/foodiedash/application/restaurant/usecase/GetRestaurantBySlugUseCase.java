package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.query.RestaurantQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetRestaurantBySlugUseCase {
    private final RestaurantRepository restaurantRepository;

    public RestaurantQueryResult execute(String slug) {
        Optional<Restaurant> restaurantOpt = restaurantRepository.findBySlug(slug);
        Restaurant restaurant = restaurantOpt
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        return new RestaurantQueryResult(
            restaurant.getId(),
            restaurant.getCode(),
            restaurant.getSlug(),
            restaurant.getName(),
            restaurant.getDescription(),
            restaurant.getAddress(),
            restaurant.getPhone(),
            restaurant.getLat(),
            restaurant.getLng(),
            restaurant.getStatus()
        );
    }
}
