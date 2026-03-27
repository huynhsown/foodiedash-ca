package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.command.DeleteCustomerAddressCommand;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteCustomerAddressUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void execute(Long customerId, DeleteCustomerAddressCommand command) {
        User user = userRepository.findByIdWithAddresses(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        if (!user.isCustomer()) {
            throw new BadRequestException("User is not a customer");
        }

        user.removeCustomerAddress(command.addressId());
        userRepository.save(user);
    }
}

