package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.query.MerchantProfileQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.model.Restaurant;
import com.ute.foodiedash.domain.restaurant.model.RestaurantBusinessHour;
import com.ute.foodiedash.domain.restaurant.enums.RestaurantStatus;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantBusinessHourRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import com.ute.foodiedash.domain.user.enums.MerchantVerificationStatus;
import com.ute.foodiedash.domain.user.enums.RoleName;
import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.domain.user.model.*;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMerchantProfileUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantBusinessHourRepository businessHourRepository;

    @InjectMocks
    private GetMerchantProfileUseCase useCase;

    @Test
    void execute_shouldReturnProfile_whenMerchantExists() {
        Long userId = 1L;
        User user = createMerchantUser(userId);
        when(userRepository.findByIdWithAll(userId)).thenReturn(Optional.of(user));

        MerchantProfileQueryResult result = useCase.execute(userId);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("merchant@test.com");
        assertThat(result.fullName()).isEqualTo("Test Merchant");
        assertThat(result.status()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.roleNames()).contains(RoleName.MERCHANT);
        assertThat(result.businessName()).isEqualTo("Test Business");
        assertThat(result.verificationStatus()).isEqualTo(MerchantVerificationStatus.PENDING);
        assertThat(result.restaurants()).isEmpty();
    }

    @Test
    void execute_shouldThrowNotFoundException_whenUserNotFound() {
        Long userId = 999L;
        when(userRepository.findByIdWithAll(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void execute_shouldThrowBadRequestException_whenUserIsNotMerchant() {
        Long userId = 1L;
        User user = createCustomerUser(userId);
        when(userRepository.findByIdWithAll(userId)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> useCase.execute(userId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("User is not a merchant");
    }

    @Test
    void execute_shouldIncludeRestaurants_whenMerchantOwnsRestaurants() {
        Long userId = 1L;
        Long restaurantId = 10L;

        User user = createMerchantUserWithRestaurants(userId, restaurantId);

        Restaurant restaurant = Restaurant.create(
                "REST-10", "Test Restaurant", "Description",
                "123 Main St", "+84123456789",
                null, null, "test-restaurant"
        );

        RestaurantBusinessHour hour = new RestaurantBusinessHour();
        hour.setRestaurantId(restaurantId);
        hour.setDayOfWeek(1);
        hour.setOpenTime(LocalTime.of(9, 0));
        hour.setCloseTime(LocalTime.of(22, 0));

        when(userRepository.findByIdWithAll(userId)).thenReturn(Optional.of(user));
        when(restaurantRepository.findByIdInAndDeletedAtIsNull(List.of(restaurantId)))
                .thenReturn(List.of(restaurant));
        when(businessHourRepository.findByRestaurantId(restaurantId, false))
                .thenReturn(List.of(hour));

        MerchantProfileQueryResult result = useCase.execute(userId);

        assertThat(result.restaurants()).hasSize(1);
        assertThat(result.restaurants().get(0).name()).isEqualTo("Test Restaurant");
        assertThat(result.restaurants().get(0).businessHours()).hasSize(1);
        assertThat(result.restaurants().get(0).businessHours().get(0).dayOfWeek()).isEqualTo(1);
        assertThat(result.restaurants().get(0).businessHours().get(0).openTime()).isEqualTo("09:00");
        assertThat(result.restaurants().get(0).businessHours().get(0).closeTime()).isEqualTo("22:00");
    }

    @Test
    void execute_shouldReturnEmptyRestaurants_whenMerchantHasNoRestaurants() {
        Long userId = 1L;
        User user = createMerchantUser(userId);
        when(userRepository.findByIdWithAll(userId)).thenReturn(Optional.of(user));

        MerchantProfileQueryResult result = useCase.execute(userId);

        assertThat(result.restaurants()).isEmpty();
    }

    private User createMerchantUser(Long userId) {
        User user = User.reconstruct(
                userId, "merchant@test.com", "+84123456789",
                "password", "Test Merchant", "https://example.com/avatar.jpg",
                UserStatus.ACTIVE,
                null,
                MerchantProfile.reconstruct(
                        100L, userId, "Test Business",
                        "LIC-001", "TAX-001",
                        "Bank A", "123456", "Holder",
                        "contact@test.com", "+84123456789",
                        MerchantVerificationStatus.PENDING,
                        Instant.now(), Instant.now(), "system", "system",
                        null, 0L
                ),
                null, null, null, null,
                List.of(UserRole.of(userId != null ? userId : 1L, RoleName.MERCHANT)),
                Instant.now(), Instant.now(), "system", "system",
                null, 0L
        );
        return user;
    }

    private User createCustomerUser(Long userId) {
        return User.reconstruct(
                userId, "customer@test.com", "+84987654321",
                "password", "Test Customer", null,
                UserStatus.ACTIVE,
                CustomerProfile.create(userId, null, null),
                null, null,
                List.of(), null, null,
                List.of(UserRole.of(userId != null ? userId : 1L, RoleName.CUSTOMER)),
                Instant.now(), Instant.now(), "system", "system",
                null, 0L
        );
    }

    private User createMerchantUserWithRestaurants(Long userId, Long restaurantId) {
        return User.reconstruct(
                userId, "merchant@test.com", "+84123456789",
                "password", "Test Merchant", "https://example.com/avatar.jpg",
                UserStatus.ACTIVE,
                null,
                MerchantProfile.reconstruct(
                        100L, userId, "Test Business",
                        "LIC-001", "TAX-001",
                        "Bank A", "123456", "Holder",
                        "contact@test.com", "+84123456789",
                        MerchantVerificationStatus.PENDING,
                        Instant.now(), Instant.now(), "system", "system",
                        null, 0L
                ),
                null,
                List.of(),
                List.of(MerchantRestaurant.create(userId, restaurantId)),
                null,
                List.of(UserRole.of(userId != null ? userId : 1L, RoleName.MERCHANT)),
                Instant.now(), Instant.now(), "system", "system",
                null, 0L
        );
    }
}
