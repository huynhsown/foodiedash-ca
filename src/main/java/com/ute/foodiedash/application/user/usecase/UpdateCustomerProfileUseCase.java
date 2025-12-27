package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.command.UpdateCustomerProfileCommand;
import com.ute.foodiedash.application.user.query.CustomerProfileQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.user.model.CustomerProfile;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateCustomerProfileUseCase {

    private final UserRepository userRepository;

    @Transactional
    public CustomerProfileQueryResult execute(Long customerId, UpdateCustomerProfileCommand command) {
        User user = userRepository.findByIdWithProfile(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        if (!user.isCustomer()) {
            throw new BadRequestException("User is not a customer");
        }

        // Update core user fields
        user.updateProfile(command.fullName(), command.phone(), command.avatarUrl());

        // Update customer-specific profile fields
        CustomerProfile profile = user.getCustomerProfile();
        if (profile != null) {
            // Preserve existing gender when client doesn't send it.
            var gender = command.gender() != null ? command.gender() : profile.getGender();
            profile.updateProfile(command.dateOfBirth(), gender);
        }

        User saved = userRepository.save(user);
        return CustomerProfileQueryResult.from(saved);
    }
}

