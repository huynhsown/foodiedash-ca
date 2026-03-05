package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.port.DomainEventPublisher;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.event.RestaurantUpdatedEvent;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RestoreRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    public void execute(Long id) {
        restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        restaurantRepository.restoreById(id);
        
        eventPublisher.publish(new RestaurantUpdatedEvent(id));
    }
}
