package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.query.DriverProfileQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetDriverProfileUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public DriverProfileQueryResult execute(Long driverId) {
        User user = userRepository.findByIdWithProfile(driverId)
                .orElseThrow(() -> new NotFoundException("Driver not found"));

        if (!user.isDriver()) {
            throw new BadRequestException("User is not a driver");
        }

        return DriverProfileQueryResult.from(user);
    }
}
