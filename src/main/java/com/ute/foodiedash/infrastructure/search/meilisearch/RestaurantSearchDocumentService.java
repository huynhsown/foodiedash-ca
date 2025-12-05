package com.ute.foodiedash.infrastructure.search.meilisearch;

import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.infrastructure.search.meilisearch.docs.RestaurantSearchDocument;

import java.util.List;

public interface RestaurantSearchDocumentService {
    RestaurantSearchDocument toSearchDocument(Restaurant restaurant);
    List<RestaurantSearchDocument> toSearchDocuments(List<Restaurant> restaurants);
}
