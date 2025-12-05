package com.ute.foodiedash.infrastructure.search.meilisearch;

import com.ute.foodiedash.infrastructure.search.meilisearch.docs.RestaurantSearchDocument;
import com.ute.foodiedash.infrastructure.search.meilisearch.dto.RestaurantSearchRequestDTO;
import com.ute.foodiedash.infrastructure.search.meilisearch.dto.RestaurantSearchResponseDTO;

import java.util.List;

public interface MeilisearchService {
    void indexRestaurant(RestaurantSearchDocument document);
    void indexRestaurants(List<RestaurantSearchDocument> documents);
    void deleteRestaurant(Long restaurantId);
    void updateRestaurant(RestaurantSearchDocument document);
    RestaurantSearchResponseDTO searchRestaurants(RestaurantSearchRequestDTO request);
    void initializeIndex();
}
