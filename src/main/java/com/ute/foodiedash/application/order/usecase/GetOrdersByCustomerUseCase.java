package com.ute.foodiedash.application.order.usecase;

import com.ute.foodiedash.application.order.query.OrderSummaryQueryResult;
import com.ute.foodiedash.application.order.query.OrderSummariesQueryResult;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.model.RestaurantImage;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantImageRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetOrdersByCustomerUseCase {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantImageRepository restaurantImageRepository;

    @Transactional(readOnly = true)
    public OrderSummariesQueryResult execute(Long customerId) {
        List<Order> orders = orderRepository.findSummariesByCustomerId(customerId);

        Set<Long> restaurantIds = orders.stream()
                .map(Order::getRestaurantId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, Restaurant> restaurantsById = loadRestaurants(restaurantIds);
        Map<Long, String> imageUrlByRestaurantId = loadPrimaryImageUrls(restaurantIds);

        List<OrderSummaryQueryResult> results = orders.stream()
                .map(order -> toOrderSummary(order, restaurantsById, imageUrlByRestaurantId))
                .filter(Objects::nonNull)
                .toList();

        return new OrderSummariesQueryResult(results);
    }

    private Map<Long, Restaurant> loadRestaurants(Set<Long> restaurantIds) {
        Map<Long, Restaurant> map = new HashMap<>();
        for (Long id : restaurantIds) {
            restaurantRepository.findByIdAndDeletedAtIsNull(id).ifPresent(r -> map.put(id, r));
        }
        return map;
    }

    private Map<Long, String> loadPrimaryImageUrls(Set<Long> restaurantIds) {
        Map<Long, String> map = new HashMap<>();
        for (Long id : restaurantIds) {
            restaurantImageRepository
                    .findFirstByRestaurantId(id)
                    .map(RestaurantImage::getImageUrl)
                    .ifPresent(url -> map.put(id, url));
        }
        return map;
    }

    private OrderSummaryQueryResult toOrderSummary(
            Order order,
            Map<Long, Restaurant> restaurantsById,
            Map<Long, String> imageUrlByRestaurantId) {
        if (order == null) {
            return null;
        }

        Long rid = order.getRestaurantId();
        Restaurant restaurant = rid != null ? restaurantsById.get(rid) : null;
        String imageUrl = rid != null ? imageUrlByRestaurantId.get(rid) : null;

        return new OrderSummaryQueryResult(
                order.getId(),
                order.getCode(),
                order.getStatus() != null ? order.getStatus().name() : null,
                order.getSubtotalAmount(),
                order.getDiscountAmount(),
                order.getDeliveryFee(),
                order.getTotalAmount(),
                order.getPlacedAt(),
                order.getAcceptedAt(),
                order.getPreparedAt(),
                order.getCancelledAt(),
                order.getCompleteAt(),
                rid,
                restaurant != null ? restaurant.getName() : null,
                imageUrl
        );
    }
}

