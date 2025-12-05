package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantCategoryCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantCategoryQueryResult;
import com.ute.foodiedash.domain.restaurant.model.RestaurantCategory;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateRestaurantCategoryUseCase {
    private final RestaurantCategoryRepository restaurantCategoryRepository;

    @Transactional
    public RestaurantCategoryQueryResult execute(CreateRestaurantCategoryCommand command) {
        RestaurantCategory category = new RestaurantCategory();
        category.setName(command.name());
        category.setIconUrl(command.iconUrl());
        category.setDescription(command.description());

        RestaurantCategory saved = restaurantCategoryRepository.save(category);

        return new RestaurantCategoryQueryResult(
            saved.getId(),
            saved.getName(),
            saved.getIconUrl(),
            saved.getDescription()
        );
    }
}
