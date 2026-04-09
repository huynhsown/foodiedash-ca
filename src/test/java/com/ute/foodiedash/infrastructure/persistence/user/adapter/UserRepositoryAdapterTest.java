package com.ute.foodiedash.infrastructure.persistence.user.adapter;

import com.ute.foodiedash.domain.common.model.PageResult;
import com.ute.foodiedash.domain.user.enums.DriverVerificationStatus;
import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.entity.UserJpaEntity;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.mapper.UserJpaMapper;
import com.ute.foodiedash.infrastructure.persistence.user.jpa.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserRepositoryAdapterTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void listDrivers_shouldReturnMappedUsers() {
        String keyword = "Son";
        UserStatus userStatus = UserStatus.ACTIVE;
        DriverVerificationStatus driverVerificationStatus = DriverVerificationStatus.PENDING;
        VehicleType vehicleType = VehicleType.MOTORBIKE;
        Instant createdFrom = Instant.parse("2025-06-25T00:00:00Z");
        Instant createdTo = Instant.parse("2027-06-26T00:00:00Z");
        Integer page = 0;
        Integer size = 10;
        String sortBy = "createdAt";
        String sortDirection = "desc";

        PageResult<User> users = userRepository.searchDrivers(keyword, userStatus, driverVerificationStatus,
                vehicleType, createdFrom, createdTo, page, size, sortBy, sortDirection);
        System.out.println("OK");
    }
}
