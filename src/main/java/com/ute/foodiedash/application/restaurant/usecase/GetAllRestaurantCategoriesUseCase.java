package com.ute.foodiedash.application.restaurant.usecase;

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

    public List<RestaurantCategoryQueryResult> execute() {
        List<RestaurantCategory> categories = restaurantCategoryRepository.findAll();
        return categories.stream()
            .map(category -> new RestaurantCategoryQueryResult(
                category.getId(),
                category.getName(),
                category.getIconUrl(),
                category.getDescription()
            ))
            .collect(Collectors.toList());
    }
}
