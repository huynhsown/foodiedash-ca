package com.ute.foodiedash.interfaces.rest.auth;

import com.ute.foodiedash.application.user.command.LoginCommand;
import com.ute.foodiedash.application.user.command.GoogleLoginCommand;
import com.ute.foodiedash.application.user.query.GoogleLoginQueryResult;
import com.ute.foodiedash.application.user.query.LoginQueryResult;
import com.ute.foodiedash.application.user.usecase.LoginWithGoogleUseCase;
import com.ute.foodiedash.interfaces.rest.auth.dto.GoogleLoginDTO;
import com.ute.foodiedash.application.user.usecase.LoginUseCase;
import com.ute.foodiedash.interfaces.rest.auth.dto.LoginDTO;
import com.ute.foodiedash.interfaces.rest.auth.dto.LoginResponseDTO;
import com.ute.foodiedash.interfaces.rest.auth.mapper.AuthDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final LoginWithGoogleUseCase loginWithGoogleUseCase;
    private final AuthDtoMapper dtoMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        LoginCommand command = dtoMapper.toCommand(dto);
        LoginQueryResult result = loginUseCase.execute(command);
        LoginResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google")
    public ResponseEntity<LoginResponseDTO> loginWithGoogle(@Valid @RequestBody GoogleLoginDTO dto) {
        GoogleLoginCommand command = dtoMapper.toCommand(dto);
        GoogleLoginQueryResult result = loginWithGoogleUseCase.execute(command);
        LoginResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
