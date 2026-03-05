package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.query.RestaurantDetailQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.model.RestaurantPreparationSetting;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantPreparationSettingRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.infrastructure.util.RestaurantUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetRestaurantSnapshotByIdUseCase {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantPreparationSettingRepository restaurantPreparationSettingRepository;
    private final RestaurantUtils restaurantUtils;

    public RestaurantDetailQueryResult execute(Long id, BigDecimal lat, BigDecimal lng) {
        Restaurant restaurant = restaurantRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        restaurant.ensureActive();

        RestaurantDetailQueryResult.RestaurantDetailQueryResultBuilder builder = RestaurantDetailQueryResult.builder()
            .id(restaurant.getId())
            .name(restaurant.getName())
            .slug(restaurant.getSlug())
            .address(restaurant.getAddress())
            .lat(restaurant.getLat())
            .lng(restaurant.getLng())
            .isOpen(restaurantUtils.checkIfRestaurantIsOpen(restaurant.getId()));

        if (lat != null && lng != null && restaurant.hasCoordinates()) {
            Double distanceKm = RestaurantUtils.calculateDistanceKm(lat, lng, restaurant.getLat(), restaurant.getLng());
            builder.distance(distanceKm);

            List<RestaurantPreparationSetting> prepSettings = restaurantPreparationSettingRepository
                .findByRestaurantId(restaurant.getId(), false);
            Integer prepTimeAvg = prepSettings.isEmpty()
                ? null
                : prepSettings.get(0).calculateAveragePrepTime();

            builder.eta(RestaurantUtils.calculateEtaMinutes(distanceKm, prepTimeAvg));
        }

        return builder.build();
    }
}
