package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantRatingCommand;
import com.ute.foodiedash.application.restaurant.port.DomainEventPublisher;
import com.ute.foodiedash.application.restaurant.query.RestaurantRatingQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.event.RestaurantUpdatedEvent;
import com.ute.foodiedash.domain.restaurant.model.RestaurantRating;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRatingRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateRestaurantRatingUseCase {
    private final RestaurantRatingRepository restaurantRatingRepository;
    private final RestaurantRepository restaurantRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    public RestaurantRatingQueryResult execute(CreateRestaurantRatingCommand command) {
        if (!restaurantRepository.existsById(command.restaurantId())) {
            throw new NotFoundException("Restaurant not found with id " + command.restaurantId());
        }

        RestaurantRating restaurantRating = new RestaurantRating();
        restaurantRating.setRestaurantId(command.restaurantId());
        restaurantRating.setRatingAvg(command.ratingAvg());
        restaurantRating.setRatingCount(command.ratingCount());

        RestaurantRating saved = restaurantRatingRepository.save(restaurantRating);

        eventPublisher.publish(new RestaurantUpdatedEvent(command.restaurantId()));

        return new RestaurantRatingQueryResult(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getRatingAvg(),
            saved.getRatingCount()
        );
    }
}
