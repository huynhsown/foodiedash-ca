package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.command.ChangeCustomerPasswordCommand;
import com.ute.foodiedash.application.user.port.PasswordHasher;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.common.exception.UnauthorizedException;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChangeCustomerPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    @Transactional
    public void execute(Long customerId, ChangeCustomerPasswordCommand command) {
        User user = userRepository.findByIdWithProfile(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        if (!user.isCustomer()) {
            throw new BadRequestException("User is not a customer");
        }

        if (!passwordHasher.verify(command.currentPassword(), user.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        user.ensureActive();
        String hashedNewPassword = passwordHasher.hash(command.newPassword());
        user.changePassword(hashedNewPassword);

        userRepository.save(user);
    }
}

