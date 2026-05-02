package com.ute.foodiedash.infrastructure.ai.tool;

import com.ute.foodiedash.application.ai.port.EmbeddingPort;
import com.ute.foodiedash.application.ai.tool.MCPTool;
import com.ute.foodiedash.application.menu.port.MenuItemVectorSearchPort;
import com.ute.foodiedash.application.menu.query.MenuItemQueryResult;
import com.ute.foodiedash.domain.menu.enums.MenuItemStatus;
import com.ute.foodiedash.domain.menu.enums.MenuStatus;
import com.ute.foodiedash.domain.menu.model.Menu;
import com.ute.foodiedash.domain.menu.model.MenuItem;
import com.ute.foodiedash.domain.menu.repository.MenuItemRepository;
import com.ute.foodiedash.domain.menu.repository.MenuRepository;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.infrastructure.util.McpToolArgsUtils;
import com.ute.foodiedash.infrastructure.util.RestaurantUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "search.engine", havingValue = "elasticsearch")
public class SearchMenuItemTool implements MCPTool {

    @Value("${elasticsearch.menu-items.vector-search.candidate-limit:500}")
    private int vectorSearchCandidateLimit;

    @Value("${elasticsearch.menu-items.vector-search.top-k:5}")
    private int vectorSearchTopK;

    private final EmbeddingPort embeddingPort;
    private final MenuItemVectorSearchPort menuItemVectorSearchPort;

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final RestaurantUtils restaurantUtils;

    @Override
    public String getName() {
        return "search_menu_item";
    }

    @Override
    public Object execute(Map<String, Object> args) {

        String itemName = McpToolArgsUtils.getString(args, "itemName");

        if (itemName == null || itemName.isBlank()) {
            throw new IllegalArgumentException("itemName is required");
        }

        Double minPrice = McpToolArgsUtils.getDouble(args, "minPrice");
        Double maxPrice = McpToolArgsUtils.getDouble(args, "maxPrice");

        BigDecimal lat = McpToolArgsUtils.getBigDecimal(args, "lat");
        BigDecimal lng = McpToolArgsUtils.getBigDecimal(args, "lng");
        Double radius = McpToolArgsUtils.getDouble(args, "radius");


        var embedding = embeddingPort.embed(itemName);
        var matches = menuItemVectorSearchPort.findMatches(
                embedding,
                vectorSearchCandidateLimit,
                vectorSearchTopK
        );

        Map<Long, Double> scoreMap =
                matches.stream()
                        .collect(Collectors.toMap(
                                MenuItemVectorSearchPort.VectorMatch::menuItemId,
                                MenuItemVectorSearchPort.VectorMatch::score
                        ));

        List<Long> menuItemIds = matches.stream()
                .map(MenuItemVectorSearchPort.VectorMatch::menuItemId)
                .toList();

        List<MenuItem> menuItems = menuItemRepository.findByIdInAndDeletedAtIsNull(menuItemIds);

        if (menuItems.isEmpty()) {
            return List.of();
        }

        List<Long> restaurantIds = menuItems.stream()
                .map(MenuItem::getRestaurantId)
                .toList();
        List<Long> menuIds = menuItems.stream()
                .map(MenuItem::getMenuId)
                .toList();

        List<Restaurant> restaurants = restaurantRepository.findByIdInAndDeletedAtIsNull(restaurantIds);
        List<Menu> menus = menuRepository.findByIdInAndDeletedAtIsNull(menuIds);

        Map<Long, Restaurant> restaurantMap =
                restaurants.stream()
                        .collect(Collectors.toMap(
                                Restaurant::getId,
                                r -> r
                        ));
        Map<Long, Menu> menuMap =
                menus.stream()
                        .collect(Collectors.toMap(
                                Menu::getId,
                                m -> m
                        ));
        Map<Long, Boolean> restaurantOpenMap =
                restaurants.stream()
                        .collect(Collectors.toMap(
                                Restaurant::getId,
                                restaurant -> restaurantUtils.checkIfRestaurantIsOpen(restaurant.getId())
                        ));

        List<MenuItemQueryResult> filteredItems =
                menuItems.stream()
                        .filter(item ->
                                item.getStatus() == MenuItemStatus.ACTIVE
                        )
                        .filter(item -> {
                            Menu menu = menuMap.get(item.getMenuId());
                            return menu != null
                                    && menu.getStatus() == MenuStatus.ACTIVE
                                    && RestaurantUtils.isMenuAvailableNow(menu);
                        })
                        .filter(item ->
                                restaurantOpenMap.getOrDefault(item.getRestaurantId(), false)
                        )
                        .filter(item -> {

                            double price =
                                    item.getPrice().doubleValue();

                            if (minPrice != null &&
                                    price < minPrice) {
                                return false;
                            }

                            if (maxPrice != null &&
                                    price > maxPrice) {
                                return false;
                            }

                            return true;
                        })
                        .filter(item -> {

                            if (lat == null ||
                                    lng == null ||
                                    radius == null) {
                                return true;
                            }

                            Restaurant restaurant =
                                    restaurantMap.get(
                                            item.getRestaurantId()
                                    );

                            if (restaurant == null ||
                                    restaurant.getLat() == null ||
                                    restaurant.getLng() == null) {
                                return false;
                            }

                            double distanceKm =
                                    RestaurantUtils.calculateDistanceKm(
                                            lat,
                                            lng,
                                            restaurant.getLat(),
                                            restaurant.getLng()
                                    );

                            return distanceKm <= radius;
                        })
                        .sorted((a, b) -> {

                            double scoreA =
                                    scoreMap.getOrDefault(
                                            a.getId(),
                                            0.0
                                    );

                            double scoreB =
                                    scoreMap.getOrDefault(
                                            b.getId(),
                                            0.0
                                    );

                            return Double.compare(scoreB, scoreA);
                        })
                        .map(item -> new MenuItemQueryResult(
                                item.getId(),
                                item.getMenuId(),
                                item.getRestaurantId(),
                                item.getName(),
                                item.getDescription(),
                                item.getPrice(),
                                item.getImageUrl(),
                                item.getStatus(),
                                null
                        ))

                        .toList();

        return filteredItems;
    }
}
