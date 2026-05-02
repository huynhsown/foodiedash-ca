package com.ute.foodiedash.infrastructure.search.meilisearch.impl;

import com.ute.foodiedash.application.ai.port.EmbeddingPort;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.model.RestaurantCategory;
import com.ute.foodiedash.domain.restaurant.model.RestaurantCategoryMap;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryMapRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantCategoryRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.infrastructure.search.meilisearch.MenuItemSearchDocumentService;
import com.ute.foodiedash.infrastructure.search.meilisearch.docs.MenuItemSearchDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MenuItemSearchDocumentServiceImpl implements MenuItemSearchDocumentService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantCategoryMapRepository restaurantCategoryMapRepository;
    private final RestaurantCategoryRepository restaurantCategoryRepository;

    private final EmbeddingPort embeddingPort;

    @Override
    public MenuItemSearchDocument toDocument(MenuItem menuItem) {

        Restaurant restaurant = restaurantRepository.findById(menuItem.getRestaurantId())
                .orElseThrow(() -> new BadRequestException("Restaurant not found"));

        List<RestaurantCategoryMap> categoryMaps = restaurantCategoryMapRepository
                .findByRestaurantId(restaurant.getId(), false);

        List<Long> categoryIds = categoryMaps.stream()
                .map(RestaurantCategoryMap::getCategoryId)
                .collect(Collectors.toList());

        List<String> categoryNames = restaurantCategoryRepository.findByIdIn(categoryIds)
                .stream()
                .map(RestaurantCategory::getName)
                .toList();

        String embeddingText = Stream.of(
                        menuItem.getName(),
                        menuItem.getDescription(),
                        restaurant.getName(),
                        String.join(" ", categoryNames)
                ).filter(Objects::nonNull)
                .collect(Collectors.joining(" "));

        List<Float> vector = embeddingPort.embed(embeddingText);

        return MenuItemSearchDocument.builder()
                .menuItemId(menuItem.getId())
                .embeddingText(embeddingText)
                .embedding(vector)
                .build();
    }
}
