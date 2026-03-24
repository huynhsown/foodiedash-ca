package com.ute.foodiedash.application.user.query;

import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.model.RestaurantBusinessHour;
import com.ute.foodiedash.domain.user.enums.MerchantVerificationStatus;
import com.ute.foodiedash.domain.user.enums.RoleName;
import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.domain.user.model.MerchantProfile;
import com.ute.foodiedash.domain.user.model.User;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record MerchantProfileQueryResult(
        Long id,
        String email,
        String phone,
        String fullName,
        String avatarUrl,
        UserStatus status,
        List<RoleName> roleNames,
        Instant accountCreatedAt,
        Instant accountUpdatedAt,
        String businessName,
        String businessLicense,
        String taxCode,
        String bankName,
        String bankAccount,
        String bankHolderName,
        String contactEmail,
        String contactPhone,
        MerchantVerificationStatus verificationStatus,
        Instant businessCreatedAt,
        Instant businessUpdatedAt,
        List<RestaurantSummary> restaurants
) {

    public static MerchantProfileQueryResult from(User user, List<Restaurant> restaurants,
                                                   java.util.Map<Long, List<RestaurantBusinessHour>> businessHoursByRestaurant) {
        MerchantProfile profile = user.getMerchantProfile();

        List<RestaurantSummary> restaurantSummaries = restaurants != null
                ? restaurants.stream()
                .map(r -> RestaurantSummary.from(r,
                businessHoursByRestaurant != null
                        ? businessHoursByRestaurant.getOrDefault(r.getId(), Collections.emptyList())
                        : Collections.emptyList()))
                .collect(Collectors.toList())
                : Collections.emptyList();

        return new MerchantProfileQueryResult(
                user.getId(),
                user.getEmail(),
                user.getPhone(),
                user.getFullName(),
                user.getAvatarUrl(),
                user.getStatus(),
                user.getRoleNames(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                profile != null ? profile.getBusinessName() : null,
                profile != null ? profile.getBusinessLicense() : null,
                profile != null ? profile.getTaxCode() : null,
                profile != null ? profile.getBankName() : null,
                profile != null ? profile.getBankAccount() : null,
                profile != null ? profile.getBankHolderName() : null,
                profile != null ? profile.getContactEmail() : null,
                profile != null ? profile.getContactPhone() : null,
                profile != null ? profile.getVerificationStatus() : null,
                profile != null ? profile.getCreatedAt() : null,
                profile != null ? profile.getUpdatedAt() : null,
                restaurantSummaries
        );
    }

    public record RestaurantSummary(
            Long id,
            String name,
            String address,
            String phone,
            String status,
            List<BusinessHourSummary> businessHours
    ) {
        public static RestaurantSummary from(Restaurant restaurant,
                                              List<RestaurantBusinessHour> businessHours) {
            List<BusinessHourSummary> hours = businessHours != null
                    ? businessHours.stream()
                    .map(BusinessHourSummary::from)
                    .collect(Collectors.toList())
                    : Collections.emptyList();

            return new RestaurantSummary(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getAddress(),
                    restaurant.getPhone(),
                    restaurant.getStatus().name(),
                    hours
            );
        }
    }

    public record BusinessHourSummary(
            Integer dayOfWeek,
            String openTime,
            String closeTime
    ) {
        public static BusinessHourSummary from(RestaurantBusinessHour hour) {
            return new BusinessHourSummary(
                    hour.getDayOfWeek(),
                    hour.getOpenTime() != null ? hour.getOpenTime().toString() : null,
                    hour.getCloseTime() != null ? hour.getCloseTime().toString() : null
            );
        }
    }
}
