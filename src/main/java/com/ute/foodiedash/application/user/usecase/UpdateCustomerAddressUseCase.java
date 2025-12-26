package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.command.UpdateCustomerAddressCommand;
import com.ute.foodiedash.application.user.query.CustomerAddressQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.user.model.CustomerAddress;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateCustomerAddressUseCase {

    private final UserRepository userRepository;

    @Transactional
    public CustomerAddressQueryResult execute(Long customerId, UpdateCustomerAddressCommand command) {
        User user = userRepository.findByIdWithAddresses(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        if (!user.isCustomer()) {
            throw new BadRequestException("User is not a customer");
        }

        CustomerAddress updatedAddress = CustomerAddress.create(
                command.label(),
                command.address(),
                command.lat(),
                command.lng(),
                command.receiverName(),
                command.receiverPhone(),
                command.note()
        );
        if (command.defaultAddress()) {
            updatedAddress.setAsDefault();
        }

        user.updateCustomerAddress(command.addressId(), updatedAddress);
        User saved = userRepository.save(user);

        CustomerAddress updated = saved.getAddresses().stream()
                .filter(a -> a.getId() != null && a.getId().equals(command.addressId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Address not found after update"));

        return CustomerAddressQueryResult.from(updated);
    }
}

