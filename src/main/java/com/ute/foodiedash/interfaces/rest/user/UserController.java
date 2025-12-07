package com.ute.foodiedash.interfaces.rest.user;

import com.ute.foodiedash.application.user.query.UserQueryResult;
import com.ute.foodiedash.application.user.usecase.RegisterCustomerUseCase;
import com.ute.foodiedash.interfaces.rest.user.dto.RegisterCustomerDTO;
import com.ute.foodiedash.interfaces.rest.user.dto.UserResponseDTO;
import com.ute.foodiedash.interfaces.rest.user.mapper.UserDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final RegisterCustomerUseCase registerCustomerUseCase;
    private final UserDtoMapper dtoMapper;

    @PostMapping("/register/customer")
    public ResponseEntity<UserResponseDTO> registerCustomer(
            @Valid @RequestBody RegisterCustomerDTO dto) {
        var command = dtoMapper.toCommand(dto);
        UserQueryResult result = registerCustomerUseCase.execute(command);
        UserResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
