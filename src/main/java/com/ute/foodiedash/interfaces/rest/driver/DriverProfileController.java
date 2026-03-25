package com.ute.foodiedash.interfaces.rest.driver;

import com.ute.foodiedash.application.user.query.DriverProfileQueryResult;
import com.ute.foodiedash.application.user.usecase.CreateDriverIdentityUseCase;
import com.ute.foodiedash.application.user.usecase.GetDriverProfileUseCase;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import com.ute.foodiedash.interfaces.rest.driver.dto.DriverProfileResponseDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.UpdateDriverIdentityDTO;
import com.ute.foodiedash.interfaces.rest.driver.mapper.DriverDtoMapper;
import com.ute.foodiedash.interfaces.rest.driver.mapper.DriverProfileDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/driver")
@RequiredArgsConstructor
public class DriverProfileController {

    private final GetDriverProfileUseCase getDriverProfileUseCase;
    private final CreateDriverIdentityUseCase createDriverIdentityUseCase;
    private final DriverProfileDtoMapper profileDtoMapper;
    private final DriverDtoMapper dtoMapper;

    @GetMapping()
    public ResponseEntity<DriverProfileResponseDTO> getMe() {
        Long driverId = SecurityContextHelper.getCurrentUserId();
        DriverProfileQueryResult result = getDriverProfileUseCase.execute(driverId);
        DriverProfileResponseDTO response = profileDtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/identity")
    public ResponseEntity<DriverProfileResponseDTO> updateIdentity(
            @Valid @RequestBody UpdateDriverIdentityDTO dto) {
        Long driverId = SecurityContextHelper.getCurrentUserId();
        var command = dtoMapper.toCommand(dto);
        DriverProfileQueryResult result = createDriverIdentityUseCase.execute(command, driverId);
        DriverProfileResponseDTO response = profileDtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }
}
