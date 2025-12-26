package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.query.CustomerAddressQueryResult;
import com.ute.foodiedash.application.user.query.CustomerAddressesQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.user.model.CustomerAddress;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListCustomerAddressesUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CustomerAddressesQueryResult execute(Long customerId) {
        User user = userRepository.findByIdWithAddresses(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        if (!user.isCustomer()) {
            throw new BadRequestException("User is not a customer");
        }

        List<CustomerAddress> addresses = user.getAddresses();
        List<CustomerAddressQueryResult> results = addresses.stream()
                .map(CustomerAddressQueryResult::from)
                .toList();

        return new CustomerAddressesQueryResult(results);
    }
}

