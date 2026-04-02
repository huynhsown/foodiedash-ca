package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.user.command.RegisterMerchantCommand;
import com.ute.foodiedash.application.user.port.PasswordHasher;
import com.ute.foodiedash.application.user.query.UserQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RegisterMerchantUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    @Transactional
    public UserQueryResult execute(RegisterMerchantCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new BadRequestException("Email already exists");
        }

        String hashedPassword = passwordHasher.hash(command.password());

        User user = User.createMerchant(
                command.email(),
                command.phone(),
                hashedPassword,
                command.fullName(),
                command.businessName(),
                command.contactEmail(),
                command.contactPhone());

        User saved = userRepository.save(user);
        return UserQueryResult.from(saved);
    }
}
