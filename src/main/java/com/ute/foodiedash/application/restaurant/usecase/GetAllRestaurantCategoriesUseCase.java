package com.ute.foodiedash.application.restaurant.usecase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ute.foodiedash.application.common.cache.CacheKey;
import com.ute.foodiedash.application.common.cache.CacheTtlSeconds;
import com.ute.foodiedash.application.common.port.CachePort;
import com.ute.foodiedash.application.restaurant.query.RestaurantCategoryQueryResult;
import com.ute.foodiedash.domain.restaurant.model.RestaurantCategory;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetAllRestaurantCategoriesUseCase {
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final CachePort cachePort;

    public List<RestaurantCategoryQueryResult> execute() {
        String cacheKey = CacheKey.restaurantCategoriesAll();
        List<RestaurantCategoryQueryResult> cached = cachePort.get(
                cacheKey,
                new TypeReference<List<RestaurantCategoryQueryResult>>() {}
        );
        if (cached != null) {
            return cached;
        }

        List<RestaurantCategory> categories = restaurantCategoryRepository.findAll();
        List<RestaurantCategoryQueryResult> result = categories.stream()
            .map(category -> new RestaurantCategoryQueryResult(
                category.getId(),
                category.getName(),
                category.getIconUrl(),
                category.getDescription()
            ))
            .collect(Collectors.toList());

        cachePort.set(cacheKey, result, CacheTtlSeconds.RESTAURANT_CATEGORIES);
        return result;
    }
}
