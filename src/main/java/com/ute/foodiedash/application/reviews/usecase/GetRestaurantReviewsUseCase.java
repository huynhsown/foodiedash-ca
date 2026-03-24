package com.ute.foodiedash.application.reviews.usecase;

import com.ute.foodiedash.application.reviews.query.RestaurantReviewsQueryResult;
import com.ute.foodiedash.application.reviews.query.ReviewCustomerQueryResult;
import com.ute.foodiedash.application.reviews.query.ReviewOrderItemQueryResult;
import com.ute.foodiedash.application.reviews.query.ReviewOrderQueryResult;
import com.ute.foodiedash.application.reviews.query.ReviewQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.order.model.Order;
import com.ute.foodiedash.domain.order.repository.OrderRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.domain.reviews.model.Review;
import com.ute.foodiedash.domain.reviews.repository.ReviewRepository;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetRestaurantReviewsUseCase {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public RestaurantReviewsQueryResult execute(Long restaurantId, int limit, int offset) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new NotFoundException("Restaurant not found");
        }

        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId, limit, offset);

        List<Long> orderIds = reviews.stream()
                .map(Review::getOrderId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, ReviewOrderQueryResult> orderMap;
        if (!orderIds.isEmpty()) {
            orderMap = orderRepository.findAllById(orderIds).stream()
                    .collect(Collectors.toMap(
                            Order::getId,
                            order -> new ReviewOrderQueryResult(
                                    order.getId(),
                                    order.getItems().stream()
                                            .map(item -> new ReviewOrderItemQueryResult(
                                                    item.getMenuItemId(),
                                                    item.getName()
                                            ))
                                            .toList()
                            )
                    ));
        } else {
            orderMap = Collections.emptyMap();
        }

        List<Long> customerIds = reviews.stream()
                .map(Review::getCustomerId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, ReviewCustomerQueryResult> customerMap;
        if (!customerIds.isEmpty()) {
            customerMap = userRepository.findBasicInfoByIds(customerIds).stream()
                    .collect(Collectors.toMap(
                            User::getId,
                            user -> new ReviewCustomerQueryResult(
                                    user.getFullName(),
                                    user.getAvatarUrl()
                            )
                    ));
        } else {
            customerMap = Collections.emptyMap();
        }

        List<ReviewQueryResult> reviewResults = reviews.stream()
                .map(review -> {
                    ReviewOrderQueryResult orderInfo = review.getOrderId() != null
                            ? orderMap.get(review.getOrderId())
                            : null;
                    ReviewCustomerQueryResult customerInfo = customerMap.get(review.getCustomerId());
                    return ReviewQueryResult.enriched(review, orderInfo, customerInfo);
                })
                .toList();

        int totalCount = reviewRepository.countByRestaurantId(restaurantId);

        return new RestaurantReviewsQueryResult(reviewResults, totalCount);
    }
}
