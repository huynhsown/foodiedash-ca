package com.ute.foodiedash.application.user.usecase;

import com.ute.foodiedash.application.auth.port.TokenGenerator;
import com.ute.foodiedash.application.user.command.GoogleLoginCommand;
import com.ute.foodiedash.application.user.port.GoogleIdentityVerifierPort;
import com.ute.foodiedash.application.user.port.PasswordHasher;
import com.ute.foodiedash.application.user.port.UserPermissionResolutionPort;
import com.ute.foodiedash.application.user.query.GoogleIdentityQueryResult;
import com.ute.foodiedash.application.user.query.GoogleLoginQueryResult;
import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.user.enums.RoleName;
import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.domain.user.model.CustomerProfile;
import com.ute.foodiedash.domain.user.model.User;
import com.ute.foodiedash.domain.user.model.UserRole;
import com.ute.foodiedash.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginWithGoogleUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private GoogleIdentityVerifierPort googleIdentityVerifierPort;
    @Mock
    private TokenGenerator tokenGenerator;
    @Mock
    private PasswordHasher passwordHasher;
    @Mock
    private UserPermissionResolutionPort userPermissionResolutionPort;

    private LoginWithGoogleUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new LoginWithGoogleUseCase(
                userRepository,
                googleIdentityVerifierPort,
                tokenGenerator,
                passwordHasher,
                userPermissionResolutionPort
        );
    }

    @Test
    void execute_shouldLoginExistingCustomer() {
        GoogleLoginCommand command = new GoogleLoginCommand("id-token");
        GoogleIdentityQueryResult identity = new GoogleIdentityQueryResult(
                "google-sub",
                "customer@example.com",
                "Customer",
                null,
                true
        );
        User existing = User.reconstruct(
                10L,
                "customer@example.com",
                null,
                "hashed",
                "Customer",
                null,
                UserStatus.ACTIVE,
                CustomerProfile.reconstruct(2L, 10L, null, null, null, null, null, null, null, 0L),
                null,
                null,
                List.of(),
                List.of(UserRole.reconstruct(10L, RoleName.CUSTOMER)),
                null, null, null, null, null, 0L
        );

        when(googleIdentityVerifierPort.verifyIdToken(command.idToken())).thenReturn(identity);
        when(userRepository.findByEmailWithRoles(identity.email())).thenReturn(Optional.of(existing));
        when(userPermissionResolutionPort.resolvePermissionNames(anyList())).thenReturn(List.of());
        when(tokenGenerator.generateToken(
                eq(10L), eq("customer@example.com"), eq(List.of("CUSTOMER")), anyList()))
                .thenReturn("jwt-token");

        GoogleLoginQueryResult result = useCase.execute(command);

        assertEquals("jwt-token", result.token());
        assertEquals(10L, result.userId());
        assertEquals("customer@example.com", result.email());
        assertEquals(null, result.avatarUrl());
    }

    @Test
    void execute_shouldRegisterIfNotExists() {
        GoogleLoginCommand command = new GoogleLoginCommand("id-token");
        GoogleIdentityQueryResult identity = new GoogleIdentityQueryResult(
                "google-sub",
                "new@example.com",
                "New User",
                null,
                true
        );

        User saved = User.reconstruct(
                20L,
                "new@example.com",
                null,
                "hashed",
                "New User",
                null,
                UserStatus.ACTIVE,
                CustomerProfile.reconstruct(3L, 20L, null, null, null, null, null, null, null, 0L),
                null,
                null,
                List.of(),
                List.of(UserRole.reconstruct(20L, RoleName.CUSTOMER)),
                null, null, null, null, null, 0L
        );

        when(googleIdentityVerifierPort.verifyIdToken(command.idToken())).thenReturn(identity);
        when(userRepository.findByEmailWithRoles(identity.email())).thenReturn(Optional.empty());
        when(passwordHasher.hash(any())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(userPermissionResolutionPort.resolvePermissionNames(anyList())).thenReturn(List.of());
        when(tokenGenerator.generateToken(
                eq(20L), eq("new@example.com"), eq(List.of("CUSTOMER")), anyList()))
                .thenReturn("jwt-token");

        GoogleLoginQueryResult result = useCase.execute(command);

        assertEquals(20L, result.userId());
        assertEquals("jwt-token", result.token());
        assertEquals(null, result.avatarUrl());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void execute_shouldRejectUnverifiedEmail() {
        GoogleLoginCommand command = new GoogleLoginCommand("id-token");
        GoogleIdentityQueryResult identity = new GoogleIdentityQueryResult(
                "google-sub",
                "customer@example.com",
                "Customer",
                null,
                false
        );
        when(googleIdentityVerifierPort.verifyIdToken(command.idToken())).thenReturn(identity);

        assertThrows(BadRequestException.class, () -> useCase.execute(command));
    }
}

