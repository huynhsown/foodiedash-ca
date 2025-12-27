package com.ute.foodiedash.interfaces.rest.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ute.foodiedash.application.user.command.GoogleLoginCommand;
import com.ute.foodiedash.application.user.query.GoogleLoginQueryResult;
import com.ute.foodiedash.application.user.usecase.LoginUseCase;
import com.ute.foodiedash.application.user.usecase.LoginWithGoogleUseCase;
import com.ute.foodiedash.domain.common.exception.UnauthorizedException;
import com.ute.foodiedash.interfaces.exception.GlobalExceptionHandler;
import com.ute.foodiedash.interfaces.rest.auth.dto.GoogleLoginDTO;
import com.ute.foodiedash.interfaces.rest.auth.dto.LoginResponseDTO;
import com.ute.foodiedash.interfaces.rest.auth.mapper.AuthDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerGoogleIntegrationTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private LoginUseCase loginUseCase;

    private LoginWithGoogleUseCase loginWithGoogleUseCase;

    private AuthDtoMapper authDtoMapper;

    @BeforeEach
    void setUp() {
        loginUseCase = mock(LoginUseCase.class);
        loginWithGoogleUseCase = mock(LoginWithGoogleUseCase.class);
        authDtoMapper = mock(AuthDtoMapper.class);
        objectMapper = new ObjectMapper();

        AuthController controller = new AuthController(loginUseCase, loginWithGoogleUseCase, authDtoMapper);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void loginWithGoogle_shouldReturnLoginPayload() throws Exception {
        GoogleLoginDTO dto = new GoogleLoginDTO();
        dto.setIdToken("valid-google-token");

        GoogleLoginCommand command = new GoogleLoginCommand("valid-google-token");
        GoogleLoginQueryResult queryResult = new GoogleLoginQueryResult(
                "jwt-token",
                99L,
                "user@gmail.com",
                "Google User",
                List.of("CUSTOMER")
        );
        LoginResponseDTO responseDTO = new LoginResponseDTO(
                "jwt-token",
                99L,
                "user@gmail.com",
                "Google User",
                List.of("CUSTOMER")
        );

        when(authDtoMapper.toCommand(any(GoogleLoginDTO.class))).thenReturn(command);
        when(loginWithGoogleUseCase.execute(command)).thenReturn(queryResult);
        when(authDtoMapper.toResponseDto(queryResult)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/auth/google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.userId").value(99))
                .andExpect(jsonPath("$.email").value("user@gmail.com"));
    }

    @Test
    void loginWithGoogle_shouldReturnUnauthorizedWhenUseCaseFails() throws Exception {
        GoogleLoginDTO dto = new GoogleLoginDTO();
        dto.setIdToken("invalid-google-token");

        GoogleLoginCommand command = new GoogleLoginCommand("invalid-google-token");
        when(authDtoMapper.toCommand(any(GoogleLoginDTO.class))).thenReturn(command);
        when(loginWithGoogleUseCase.execute(command)).thenThrow(new UnauthorizedException("Google token verification failed"));

        mockMvc.perform(post("/api/v1/auth/google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Google token verification failed"));
    }
}

