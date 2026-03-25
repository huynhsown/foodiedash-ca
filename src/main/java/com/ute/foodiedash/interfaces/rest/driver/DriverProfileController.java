package com.ute.foodiedash.interfaces.rest.driver;

import com.ute.foodiedash.application.user.query.DriverProfileQueryResult;
import com.ute.foodiedash.application.user.usecase.*;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import com.ute.foodiedash.interfaces.rest.driver.dto.DriverProfileResponseDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.UpdateDriverBankDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.UpdateDriverIdentityDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.UpdateDriverLicenseDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.UpdateDriverVehicleDTO;
import com.ute.foodiedash.interfaces.rest.driver.mapper.DriverDtoMapper;
import com.ute.foodiedash.interfaces.rest.driver.mapper.DriverProfileDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/driver")
@RequiredArgsConstructor
public class DriverProfileController {

    private final GetDriverProfileUseCase getDriverProfileUseCase;
    private final CreateDriverIdentityUseCase createDriverIdentityUseCase;
    private final UpdateDriverLicenseUseCase updateDriverLicenseUseCase;
    private final UpdateDriverVehicleUseCase updateDriverVehicleUseCase;
    private final UpdateDriverBankUseCase updateDriverBankUseCase;
    private final DriverProfileDtoMapper profileDtoMapper;
    private final DriverDtoMapper dtoMapper;
    private final SubmitDriverProfileUseCase submitDriverProfileUseCase;
    private final ApproveDriverProfileUseCase approveDriverProfileUseCase;

    @GetMapping()
    public ResponseEntity<DriverProfileResponseDTO> getMe() {
        Long driverId = SecurityContextHelper.getCurrentUserId();
        DriverProfileQueryResult result = getDriverProfileUseCase.execute(driverId);
        DriverProfileResponseDTO response = profileDtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile/identity")
    public ResponseEntity<DriverProfileResponseDTO> updateIdentity(
            @Valid @RequestBody UpdateDriverIdentityDTO dto) {
        Long driverId = SecurityContextHelper.getCurrentUserId();
        var command = dtoMapper.toCommand(dto);
        DriverProfileQueryResult result = createDriverIdentityUseCase.execute(command, driverId);
        DriverProfileResponseDTO response = profileDtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile/license")
    public ResponseEntity<DriverProfileResponseDTO> updateLicense(
            @Valid @RequestBody UpdateDriverLicenseDTO dto) {
        Long driverId = SecurityContextHelper.getCurrentUserId();
        var command = dtoMapper.toCommand(dto);
        DriverProfileQueryResult result = updateDriverLicenseUseCase.execute(command, driverId);
        DriverProfileResponseDTO response = profileDtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile/vehicle")
    public ResponseEntity<DriverProfileResponseDTO> updateVehicle(
            @Valid @RequestBody UpdateDriverVehicleDTO dto) {
        Long driverId = SecurityContextHelper.getCurrentUserId();
        var command = dtoMapper.toCommand(dto);
        DriverProfileQueryResult result = updateDriverVehicleUseCase.execute(command, driverId);
        DriverProfileResponseDTO response = profileDtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile/bank")
    public ResponseEntity<DriverProfileResponseDTO> updateBank(
            @Valid @RequestBody UpdateDriverBankDTO dto) {
        Long driverId = SecurityContextHelper.getCurrentUserId();
        var command = dtoMapper.toCommand(dto);
        DriverProfileQueryResult result = updateDriverBankUseCase.execute(command, driverId);
        DriverProfileResponseDTO response = profileDtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile/submit")
    public ResponseEntity<Void> submit() {
        Long driverId = SecurityContextHelper.getCurrentUserId();
        submitDriverProfileUseCase.execute(driverId);
        return ResponseEntity.ok().build();
    }
}
