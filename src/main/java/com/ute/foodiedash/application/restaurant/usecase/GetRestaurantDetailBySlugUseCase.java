package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.query.RestaurantDetailQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.model.RestaurantCategory;
import com.ute.foodiedash.domain.restaurant.model.RestaurantCategoryMap;
import com.ute.foodiedash.domain.restaurant.model.RestaurantImage;
import com.ute.foodiedash.domain.restaurant.model.RestaurantPreparationSetting;
import com.ute.foodiedash.domain.restaurant.model.RestaurantRating;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryMapRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantImageRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantPreparationSettingRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRatingRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.infrastructure.util.RestaurantUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetRestaurantDetailBySlugUseCase {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantRatingRepository restaurantRatingRepository;
    private final RestaurantImageRepository restaurantImageRepository;
    private final RestaurantCategoryMapRepository restaurantCategoryMapRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final RestaurantPreparationSettingRepository restaurantPreparationSettingRepository;
    private final RestaurantUtils restaurantUtils;

    public RestaurantDetailQueryResult execute(String slug, BigDecimal lat, BigDecimal lng) {
        Restaurant restaurant = restaurantRepository.findBySlug(slug)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        restaurant.ensureActive();

        RestaurantDetailQueryResult.RestaurantDetailQueryResultBuilder builder = RestaurantDetailQueryResult.builder()
            .id(restaurant.getId())
            .name(restaurant.getName())
            .slug(restaurant.getSlug())
            .address(restaurant.getAddress())
            .lat(restaurant.getLat())
            .lng(restaurant.getLng());

        List<RestaurantRating> ratings = restaurantRatingRepository.findByRestaurantId(restaurant.getId(), false);
        if (!ratings.isEmpty()) {
            RestaurantRating rating = ratings.get(0);
            builder.ratingAvg(rating.getRatingAvgAsDouble());
        }

        restaurantImageRepository.findFirstByRestaurantId(restaurant.getId())
            .ifPresent(image -> builder.imageUrl(image.getImageUrl()));

        builder.isOpen(restaurantUtils.checkIfRestaurantIsOpen(restaurant.getId()));

        List<RestaurantCategoryMap> categoryMaps = restaurantCategoryMapRepository
            .findByRestaurantId(restaurant.getId(), false);
        List<Long> categoryIds = categoryMaps.stream()
            .map(RestaurantCategoryMap::getCategoryId)
            .collect(Collectors.toList());
        List<String> categoryNames = restaurantCategoryRepository.findByIdIn(categoryIds)
            .stream()
            .map(RestaurantCategory::getName)
            .collect(Collectors.toList());
        builder.categories(categoryNames);

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
