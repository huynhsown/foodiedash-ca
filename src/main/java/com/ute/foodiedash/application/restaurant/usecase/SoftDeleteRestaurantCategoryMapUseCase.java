package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.port.DomainEventPublisher;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.event.RestaurantUpdatedEvent;
import com.ute.foodiedash.domain.restaurant.model.RestaurantCategoryMap;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SoftDeleteRestaurantCategoryMapUseCase {
    private final RestaurantCategoryMapRepository restaurantCategoryMapRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    public void execute(Long id) {
        RestaurantCategoryMap categoryMap = restaurantCategoryMapRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant Category Map not found"));
        
        Long restaurantId = categoryMap.getRestaurantId();
        restaurantCategoryMapRepository.softDeleteById(id);

        eventPublisher.publish(new RestaurantUpdatedEvent(restaurantId));
    }
}
