package com.ute.foodiedash.interfaces.rest.search;

import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.infrastructure.search.meilisearch.MeilisearchService;
import com.ute.foodiedash.infrastructure.search.meilisearch.RestaurantSearchDocumentService;
import com.ute.foodiedash.infrastructure.search.meilisearch.dto.RestaurantSearchRequestDTO;
import com.ute.foodiedash.infrastructure.search.meilisearch.dto.RestaurantSearchResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class SearchController {

    private final MeilisearchService meilisearchService;
    private final RestaurantSearchDocumentService restaurantSearchDocumentService;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/search")
    public ResponseEntity<RestaurantSearchResponseDTO> searchRestaurants(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) BigDecimal lat,
            @RequestParam(required = false) BigDecimal lng,
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) Boolean isOpen,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Integer radius) {
        
        RestaurantSearchRequestDTO request = new RestaurantSearchRequestDTO();
        request.setQuery(query);
        request.setCategoryName(categoryName);
        request.setLat(lat);
        request.setLng(lng);
        request.setLimit(limit);
        request.setOffset(offset);
        request.setIsOpen(isOpen);
        request.setMinRating(minRating);
        request.setRadius(radius);
        
        RestaurantSearchResponseDTO response = meilisearchService.searchRestaurants(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/index")
    public ResponseEntity<?> indexRestaurant(
            @PathVariable Long id
    ) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        var document = restaurantSearchDocumentService.toSearchDocument(restaurant);
        meilisearchService.indexRestaurant(document);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search/reindex")
    public ResponseEntity<?> reindexAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAllActive();
        var documents = restaurants.stream()
                .map(restaurantSearchDocumentService::toSearchDocument)
                .toList();
        meilisearchService.indexRestaurants(documents);
        return ResponseEntity.ok().build();
    }
}
