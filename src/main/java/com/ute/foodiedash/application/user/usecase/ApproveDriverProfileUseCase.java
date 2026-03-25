package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApproveDriverProfileUseCase {
    private final UserRepository userRepository;

    public void execute (Long driverId) {
        User user = userRepository.findByIdWithProfile(driverId)
                .orElseThrow(() -> new NotFoundException("Driver not found"));

        if (!user.isDriver()) {
            throw new BadRequestException("User is not a driver");
        }

        user.getDriverProfile().approve();
        user = userRepository.save(user);
    }
}
