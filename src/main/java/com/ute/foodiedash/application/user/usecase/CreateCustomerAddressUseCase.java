package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.command.CreateCustomerAddressCommand;
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
public class CreateCustomerAddressUseCase {

    private final UserRepository userRepository;

    @Transactional
    public CustomerAddressQueryResult execute(Long customerId, CreateCustomerAddressCommand command) {
        User user = userRepository.findByIdWithAddresses(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        if (!user.isCustomer()) {
            throw new BadRequestException("User is not a customer");
        }

        CustomerAddress address = CustomerAddress.create(
                command.label(),
                command.address(),
                command.lat(),
                command.lng(),
                command.receiverName(),
                command.receiverPhone(),
                command.note()
        );

        if (command.defaultAddress()) {
            address.setAsDefault();
        }

        user.addCustomerAddress(address);
        User saved = userRepository.save(user);

        var savedAddresses = saved.getAddresses();
        if (savedAddresses == null || savedAddresses.isEmpty()) {
            throw new NotFoundException("Address not created");
        }
        CustomerAddress created = savedAddresses.get(savedAddresses.size() - 1);
        return CustomerAddressQueryResult.from(created);
    }
}

