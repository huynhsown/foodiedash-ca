package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.auth.port.TokenGenerator;
import com.ute.foodiedash.application.user.command.LoginCommand;
import com.ute.foodiedash.application.user.port.PasswordHasher;
import com.ute.foodiedash.application.user.query.LoginQueryResult;
import com.ute.foodiedash.domain.common.exception.UnauthorizedException;
import com.ute.foodiedash.domain.user.enums.RoleName;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;

    @Transactional(readOnly = true)
    public LoginQueryResult execute(LoginCommand command) {
        User user = userRepository.findByEmailWithRoles(command.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordHasher.verify(command.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        List<String> roleNames = user.getRoleNames().stream()
                .map(RoleName::name)
                .collect(Collectors.toList());

        String token = tokenGenerator.generateToken(user.getId(), user.getEmail(), roleNames);

        return new LoginQueryResult(
                token,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                roleNames
        );
    }
}
