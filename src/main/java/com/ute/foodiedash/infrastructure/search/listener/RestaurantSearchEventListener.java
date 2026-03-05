package com.ute.foodiedash.infrastructure.search.listener;

import com.ute.foodiedash.domain.restaurant.event.RestaurantCreatedEvent;
import com.ute.foodiedash.domain.restaurant.event.RestaurantDeletedEvent;
import com.ute.foodiedash.domain.restaurant.event.RestaurantUpdatedEvent;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.infrastructure.search.meilisearch.MeilisearchService;
import com.ute.foodiedash.infrastructure.search.meilisearch.RestaurantSearchDocumentService;
import com.ute.foodiedash.infrastructure.search.meilisearch.docs.RestaurantSearchDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantSearchEventListener {

    private final MeilisearchService meilisearchService;
    private final RestaurantSearchDocumentService restaurantSearchDocumentService;
    private final RestaurantRepository restaurantRepository;

    @Async
    @EventListener
    public void handleRestaurantCreated(RestaurantCreatedEvent event) {
        try {
            log.debug("Handling RestaurantCreatedEvent for restaurant id: {}", event.getRestaurantId());
            Restaurant restaurant = restaurantRepository.findById(event.getRestaurantId())
                    .orElse(null);
            if (restaurant != null) {
                RestaurantSearchDocument document = restaurantSearchDocumentService.toSearchDocument(restaurant);
                meilisearchService.indexRestaurant(document);
                log.debug("Successfully indexed restaurant {} after creation", event.getRestaurantId());
            }
        } catch (Exception e) {
            log.error("Failed to index restaurant {} after creation", event.getRestaurantId(), e);
        }
    }

    @Async
    @EventListener
    public void handleRestaurantUpdated(RestaurantUpdatedEvent event) {
        try {
            log.debug("Handling RestaurantUpdatedEvent for restaurant id: {}", event.getRestaurantId());
            Restaurant restaurant = restaurantRepository.findById(event.getRestaurantId())
                    .orElse(null);
            if (restaurant != null) {
                RestaurantSearchDocument document = restaurantSearchDocumentService.toSearchDocument(restaurant);
                meilisearchService.updateRestaurant(document);
                log.debug("Successfully updated index for restaurant {}", event.getRestaurantId());
            }
        } catch (Exception e) {
            log.error("Failed to update index for restaurant {}", event.getRestaurantId(), e);
        }
    }

    @Async
    @EventListener
    public void handleRestaurantDeleted(RestaurantDeletedEvent event) {
        try {
            log.debug("Handling RestaurantDeletedEvent for restaurant id: {}", event.getRestaurantId());
            meilisearchService.deleteRestaurant(event.getRestaurantId());
            log.debug("Successfully deleted restaurant {} from index", event.getRestaurantId());
        } catch (Exception e) {
            log.error("Failed to delete restaurant {} from index", event.getRestaurantId(), e);
        }
    }
}
