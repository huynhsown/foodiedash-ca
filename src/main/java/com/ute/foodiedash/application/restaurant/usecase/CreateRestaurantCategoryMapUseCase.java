package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantCategoryMapCommand;
import com.ute.foodiedash.application.restaurant.port.DomainEventPublisher;
import com.ute.foodiedash.application.restaurant.query.RestaurantCategoryMapQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.event.RestaurantUpdatedEvent;
import com.ute.foodiedash.domain.restaurant.model.RestaurantCategoryMap;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryMapRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateRestaurantCategoryMapUseCase {
    private final RestaurantCategoryMapRepository restaurantCategoryMapRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    public RestaurantCategoryMapQueryResult execute(CreateRestaurantCategoryMapCommand command) {
        if (!restaurantRepository.existsById(command.restaurantId())) {
            throw new NotFoundException("Restaurant not found with id " + command.restaurantId());
        }
        if (!restaurantCategoryRepository.existsById(command.categoryId())) {
            throw new NotFoundException("Restaurant Category not found with id " + command.categoryId());
        }

        RestaurantCategoryMap categoryMap = new RestaurantCategoryMap();
        categoryMap.setRestaurantId(command.restaurantId());
        categoryMap.setCategoryId(command.categoryId());

        RestaurantCategoryMap saved = restaurantCategoryMapRepository.save(categoryMap);

        eventPublisher.publish(new RestaurantUpdatedEvent(command.restaurantId()));

        return new RestaurantCategoryMapQueryResult(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getCategoryId()
        );
    }
}
