package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.auth.port.TokenGenerator;
import com.ute.foodiedash.application.user.command.GoogleLoginCommand;
import com.ute.foodiedash.application.user.port.GoogleIdentityVerifierPort;
import com.ute.foodiedash.application.user.port.PasswordHasher;
import com.ute.foodiedash.application.user.query.GoogleIdentityQueryResult;
import com.ute.foodiedash.application.user.query.GoogleLoginQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.user.enums.Gender;
import com.ute.foodiedash.domain.user.enums.RoleName;
import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoginWithGoogleUseCase {

    private final UserRepository userRepository;
    private final GoogleIdentityVerifierPort googleIdentityVerifierPort;
    private final TokenGenerator tokenGenerator;
    private final PasswordHasher passwordHasher;

    @Transactional
    public GoogleLoginQueryResult execute(GoogleLoginCommand command) {
        GoogleIdentityQueryResult identity = googleIdentityVerifierPort.verifyIdToken(command.idToken());
        if (!identity.emailVerified()) {
            throw new BadRequestException("Google email is not verified");
        }

        User user = userRepository.findByEmailWithRoles(identity.email())
                .map(this::handleExistingUser)
                .orElseGet(() -> createGoogleCustomer(identity));

        List<String> roleNames = user.getRoleNames().stream()
                .map(RoleName::name)
                .collect(Collectors.toList());

        String token = tokenGenerator.generateToken(user.getId(), user.getEmail(), roleNames);
        String avatarUrl = user.getAvatarUrl() != null && !user.getAvatarUrl().isBlank()
                ? user.getAvatarUrl()
                : identity.picture();
        return new GoogleLoginQueryResult(
                token,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                avatarUrl,
                roleNames
        );
    }

    private User handleExistingUser(User user) {
        if (!user.hasRole(RoleName.CUSTOMER)) {
            throw new BadRequestException("Google account is linked to non-customer user");
        }

        if (user.getStatus() == UserStatus.PENDING_VERIFICATION) {
            user.activate();
            return userRepository.save(user);
        }

        return user;
    }

    private User createGoogleCustomer(GoogleIdentityQueryResult identity) {
        String generatedPassword = generateInternalPassword();
        User user = User.createCustomer(
                identity.email(),
                null,
                passwordHasher.hash(generatedPassword),
                identity.name() != null && !identity.name().isBlank() ? identity.name() : identity.email(),
                null,
                Gender.OTHER
        );
        user.activate();
        return userRepository.save(user);
    }

    private String generateInternalPassword() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return "GG-" + HexFormat.of().formatHex(bytes);
    }
}

