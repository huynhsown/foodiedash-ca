package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.query.CustomerProfileQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetCustomerProfileUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CustomerProfileQueryResult execute(Long customerId) {
        User user = userRepository.findByIdWithProfile(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        if (!user.isCustomer()) {
            throw new BadRequestException("User is not a customer");
        }

        return CustomerProfileQueryResult.from(user);
    }
}

