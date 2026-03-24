package com.ute.foodiedash.infrastructure.search.listener;

import com.ute.foodiedash.domain.restaurant.event.RestaurantCreatedEvent;
import com.ute.foodiedash.domain.restaurant.event.RestaurantDeletedEvent;
import com.ute.foodiedash.domain.restaurant.event.RestaurantUpdatedEvent;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.infrastructure.search.meilisearch.MeilisearchService;
import com.ute.foodiedash.infrastructure.search.meilisearch.RestaurantSearchDocumentService;
import com.ute.foodiedash.infrastructure.search.meilisearch.docs.RestaurantSearchDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantSearchEventListenerTest {

    @Mock
    private MeilisearchService meilisearchService;
    @Mock
    private RestaurantSearchDocumentService restaurantSearchDocumentService;
    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantSearchEventListener listener;

    @Test
    void handleRestaurantCreated_shouldFetchRestaurantAndIndexDocument() {
        RestaurantCreatedEvent event = new RestaurantCreatedEvent(1L);
        Restaurant restaurant = mock(Restaurant.class);
        RestaurantSearchDocument document = mock(RestaurantSearchDocument.class);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantSearchDocumentService.toSearchDocument(restaurant)).thenReturn(document);

        listener.handleRestaurantCreated(event);

        verify(restaurantRepository).findById(1L);
        verify(meilisearchService).indexRestaurant(document);
    }

    @Test
    void handleRestaurantUpdated_shouldFetchRestaurantAndUpdateDocument() {
        RestaurantUpdatedEvent event = new RestaurantUpdatedEvent(2L);
        Restaurant restaurant = mock(Restaurant.class);
        RestaurantSearchDocument document = mock(RestaurantSearchDocument.class);
        when(restaurantRepository.findById(2L)).thenReturn(Optional.of(restaurant));
        when(restaurantSearchDocumentService.toSearchDocument(restaurant)).thenReturn(document);

        listener.handleRestaurantUpdated(event);

        verify(restaurantRepository).findById(2L);
        verify(meilisearchService).updateRestaurant(document);
    }

    @Test
    void handleRestaurantDeleted_shouldCallDeleteOnSearchService() {
        RestaurantDeletedEvent event = new RestaurantDeletedEvent(3L);

        listener.handleRestaurantDeleted(event);

        verify(meilisearchService).deleteRestaurant(3L);
    }

    @Test
    void handleRestaurantCreated_shouldNotFailWhenRestaurantNotFound() {
        RestaurantCreatedEvent event = new RestaurantCreatedEvent(99L);
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        listener.handleRestaurantCreated(event);

        verify(meilisearchService, never()).indexRestaurant(any());
    }
}
