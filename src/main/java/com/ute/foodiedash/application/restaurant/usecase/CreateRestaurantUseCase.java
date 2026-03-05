package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantCommand;
import com.ute.foodiedash.application.restaurant.port.DomainEventPublisher;
import com.ute.foodiedash.application.restaurant.query.RestaurantQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.restaurant.event.RestaurantCreatedEvent;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.domain.restaurant.service.SlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final DomainEventPublisher eventPublisher;
    private final SlugGenerator slugGenerator;

    @Transactional
    public RestaurantQueryResult execute(CreateRestaurantCommand command) {
        // Generate slug if not provided
        String slug = command.slug();
        if (slug == null || slug.isBlank()) {
            slug = slugGenerator.generateUniqueSlug(command.name());
        } else if (restaurantRepository.existsBySlug(slug)) {
            throw new BadRequestException("Slug already exists");
        }

        Restaurant restaurant = Restaurant.create(
            command.code(), command.name(), command.description(),
            command.address(), command.phone(),
            command.lat(), command.lng(), slug
        );

        Restaurant saved = restaurantRepository.save(restaurant);

        // Publish domain event
        eventPublisher.publish(new RestaurantCreatedEvent(saved.getId()));

        return new RestaurantQueryResult(
            saved.getId(),
            saved.getCode(),
            saved.getSlug(),
            saved.getName(),
            saved.getDescription(),
            saved.getAddress(),
            saved.getPhone(),
            saved.getLat(),
            saved.getLng(),
            saved.getStatus()
        );
    }
}
