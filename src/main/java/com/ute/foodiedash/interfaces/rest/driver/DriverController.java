package com.ute.foodiedash.interfaces.rest.driver;

import com.ute.foodiedash.application.user.query.UserQueryResult;
import com.ute.foodiedash.application.user.usecase.RegisterDriverUseCase;
import com.ute.foodiedash.interfaces.rest.driver.dto.RegisterDriverDTO;
import com.ute.foodiedash.interfaces.rest.driver.mapper.DriverDtoMapper;
import com.ute.foodiedash.interfaces.rest.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/driver")
@RequiredArgsConstructor
public class DriverController {
    private final RegisterDriverUseCase registerDriverUseCase;
    private final DriverDtoMapper dtoMapper;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerDriver(
            @Valid @RequestBody RegisterDriverDTO dto) {
        var command = dtoMapper.toCommand(dto);
        UserQueryResult result = registerDriverUseCase.execute(command);
        UserResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
