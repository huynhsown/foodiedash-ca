package com.ute.foodiedash.domain.user.repository;

import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import com.ute.foodiedash.domain.user.model.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAllById(List<Long> ids);
    List<User> findBasicInfoByIds(List<Long> ids);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByIdWithRoles(Long id);
    Optional<User> findByIdWithAddresses(Long id);
    Optional<User> findByIdWithProfile(Long id);
    Optional<User> findByIdWithAll(Long id);
    Optional<User> findByEmailWithRoles(String email);
    boolean existsMerchantRestaurant(Long userId, Long restaurantId);

    List<User> listDrivers(String keyword, UserStatus userStatus, DriverVerificationStatus driverVerificationStatus,
                           VehicleType vehicleType, Instant createdFrom, Instant createdTo, Integer page, Integer size,
                           String sortBy, String sortDirection);

    void softDeleteById(Long id);
    void restoreById(Long id);
}
