package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.query.MerchantProfileQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.model.RestaurantBusinessHour;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantBusinessHourRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.domain.user.model.MerchantRestaurant;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetMerchantProfileUseCase {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantBusinessHourRepository businessHourRepository;

    @Transactional(readOnly = true)
    public MerchantProfileQueryResult execute(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.isMerchant()) {
            throw new BadRequestException("User is not a merchant");
        }

        List<Long> restaurantIds = user.getMerchantRestaurants() != null
                ? user.getMerchantRestaurants().stream()
                .map(MerchantRestaurant::getRestaurantId)
                .collect(Collectors.toList())
                : Collections.emptyList();

        List<Restaurant> restaurants = restaurantIds.isEmpty()
                ? Collections.emptyList()
                : restaurantRepository.findByIdInAndDeletedAtIsNull(restaurantIds);

        Map<Long, List<RestaurantBusinessHour>> businessHoursByRestaurant =
                restaurants.stream()
                        .collect(Collectors.toMap(
                                Restaurant::getId,
                                r -> businessHourRepository.findByRestaurantId(r.getId(), false)
                        ));

        return MerchantProfileQueryResult.from(user, restaurants, businessHoursByRestaurant);
    }
}
