package com.ute.foodiedash.infrastructure.search.meilisearch.impl;

import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.model.RestaurantCategory;
import com.ute.foodiedash.domain.restaurant.model.RestaurantCategoryMap;
import com.ute.foodiedash.domain.restaurant.model.RestaurantPreparationSetting;
import com.ute.foodiedash.domain.restaurant.model.RestaurantRating;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantBusinessHourRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryMapRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantPreparationSettingRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRatingRepository;
import com.ute.foodiedash.infrastructure.search.meilisearch.RestaurantSearchDocumentService;
import com.ute.foodiedash.infrastructure.search.meilisearch.docs.RestaurantSearchDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantSearchDocumentServiceImpl implements RestaurantSearchDocumentService {

    private final RestaurantRatingRepository restaurantRatingRepository;
    private final RestaurantPreparationSettingRepository restaurantPreparationSettingRepository;
    private final RestaurantCategoryMapRepository restaurantCategoryMapRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantBusinessHourRepository restaurantBusinessHourRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;

    @Override
    public RestaurantSearchDocument toSearchDocument(Restaurant restaurant) {
        RestaurantSearchDocument document = RestaurantSearchDocument.builder()
            .id(restaurant.getId())
            .name(restaurant.getName())
            .slug(restaurant.getSlug())
            .build();

        if (restaurant.getLat() != null && restaurant.getLng() != null) {
            RestaurantSearchDocument.GeoPoint geo = new RestaurantSearchDocument.GeoPoint(
                restaurant.getLat().doubleValue(),
                restaurant.getLng().doubleValue()
            );
            document.setGeo(geo);
        }

        List<RestaurantRating> ratings = restaurantRatingRepository.findByRestaurantId(restaurant.getId(), false);
        if (!ratings.isEmpty()) {
            RestaurantRating rating = ratings.get(0);
            document.setRatingAvg(rating.getRatingAvg() != null ? rating.getRatingAvg().doubleValue() : null);
            document.setRatingCount(rating.getRatingCount());
        }

        List<RestaurantPreparationSetting> prepSettings = restaurantPreparationSettingRepository
            .findByRestaurantId(restaurant.getId(), false);
        if (!prepSettings.isEmpty()) {
            RestaurantPreparationSetting prepSetting = prepSettings.get(0);
            int avgPrepTime = (prepSetting.getPrepTimeMin() + prepSetting.getPrepTimeMax()) / 2;
            document.setPrepTimeAvg(avgPrepTime);
        }

        List<RestaurantCategoryMap> categoryMaps = restaurantCategoryMapRepository
            .findByRestaurantId(restaurant.getId(), false);
        List<Long> categoryIds = categoryMaps.stream()
            .map(RestaurantCategoryMap::getCategoryId)
            .collect(Collectors.toList());
        List<String> categoryNames = restaurantCategoryRepository.findByIdIn(categoryIds)
                .stream()
                .map(RestaurantCategory::getName)
                .toList();
        document.setCategories(categoryNames);

        List<MenuItem> restaurantMenuItems = menuItemRepository.findByRestaurantId(restaurant.getId(), false);
        List<String> menuItemNames = restaurantMenuItems.stream()
            .map(MenuItem::getName)
            .collect(Collectors.toList());
        document.setMenuItems(menuItemNames);

        return document;
    }

    @Override
    public List<RestaurantSearchDocument> toSearchDocuments(List<Restaurant> restaurants) {
        return restaurants.stream()
            .map(this::toSearchDocument)
            .collect(Collectors.toList());
    }
}
