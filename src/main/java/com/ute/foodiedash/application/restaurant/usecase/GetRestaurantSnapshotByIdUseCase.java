package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.common.cache.CacheKey;
import com.ute.foodiedash.application.common.cache.CacheTtlSeconds;
import com.ute.foodiedash.application.common.port.CachePort;
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
    private final CachePort cachePort;

    public RestaurantDetailQueryResult execute(Long id, BigDecimal lat, BigDecimal lng) {
        String cacheKey = CacheKey.restaurantSnapshotById(id, lat, lng);
        RestaurantDetailQueryResult cached = cachePort.get(cacheKey, RestaurantDetailQueryResult.class);
        if (cached != null) {
            return cached;
        }

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

        RestaurantDetailQueryResult result = builder.build();
        cachePort.set(cacheKey, result, CacheTtlSeconds.RESTAURANT_SNAPSHOT_BY_ID);
        return result;
    }
}
