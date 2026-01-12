package com.ute.foodiedash.interfaces.rest.user;

import com.ute.foodiedash.application.user.command.DeleteCustomerAddressCommand;
import com.ute.foodiedash.application.user.command.CreateCustomerAddressCommand;
import com.ute.foodiedash.application.user.command.UpdateCustomerAddressCommand;
import com.ute.foodiedash.application.user.command.UpdateCustomerProfileCommand;
import com.ute.foodiedash.application.user.command.ChangeCustomerPasswordCommand;
import com.ute.foodiedash.application.user.query.CustomerAddressQueryResult;
import com.ute.foodiedash.application.user.query.CustomerAddressesQueryResult;
import com.ute.foodiedash.application.user.query.CustomerProfileQueryResult;
import com.ute.foodiedash.application.user.usecase.*;
import com.ute.foodiedash.interfaces.rest.user.dto.*;
import com.ute.foodiedash.interfaces.rest.user.mapper.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ORDER_VIEW_OWN','CART_MANAGE','RESTAURANT_VIEW','RATING_CREATE')")
public class CustomerProfileController {

    private final GetCustomerProfileUseCase getCustomerProfileUseCase;
    private final UpdateCustomerProfileUseCase updateCustomerProfileUseCase;

    private final CreateCustomerAddressUseCase createCustomerAddressUseCase;
    private final ListCustomerAddressesUseCase listCustomerAddressesUseCase;
    private final UpdateCustomerAddressUseCase updateCustomerAddressUseCase;
    private final DeleteCustomerAddressUseCase deleteCustomerAddressUseCase;

    private final ChangeCustomerPasswordUseCase changeCustomerPasswordUseCase;

    private final CustomerProfileDtoMapper profileDtoMapper;
    private final CustomerAddressDtoMapper addressDtoMapper;
    private final CustomerPasswordDtoMapper passwordDtoMapper;

    private Long getCurrentCustomerId() {
        return SecurityContextHelper.getCurrentUserId();
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerProfileResponseDTO> getMe() {
        CustomerProfileQueryResult result = getCustomerProfileUseCase.execute(getCurrentCustomerId());
        CustomerProfileResponseDTO response = profileDtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<CustomerProfileResponseDTO> updateMe(
            @Valid @RequestBody UpdateCustomerProfileDTO dto) {
        Long customerId = getCurrentCustomerId();
        UpdateCustomerProfileCommand command = profileDtoMapper.toCommand(dto);
        CustomerProfileQueryResult result = updateCustomerProfileUseCase.execute(customerId, command);
        CustomerProfileResponseDTO response = profileDtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addresses")
    public ResponseEntity<CustomerAddressResponseDTO> createAddress(
            @Valid @RequestBody CreateCustomerAddressDTO dto) {
        Long customerId = getCurrentCustomerId();
        CreateCustomerAddressCommand command = addressDtoMapper.toCreateCommand(dto);
        CustomerAddressQueryResult result = createCustomerAddressUseCase.execute(customerId, command);
        CustomerAddressResponseDTO response = addressDtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/addresses")
    public ResponseEntity<CustomerAddressesResponseDTO> listAddresses() {
        CustomerAddressesQueryResult result = listCustomerAddressesUseCase.execute(getCurrentCustomerId());
        CustomerAddressesResponseDTO response = addressDtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<CustomerAddressResponseDTO> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerAddressDTO dto) {
        Long customerId = getCurrentCustomerId();
        UpdateCustomerAddressCommand command = addressDtoMapper.toUpdateCommand(id, dto);
        CustomerAddressQueryResult result = updateCustomerAddressUseCase.execute(customerId, command);
        CustomerAddressResponseDTO response = addressDtoMapper.toResponseDto(result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        deleteCustomerAddressUseCase.execute(getCurrentCustomerId(), new DeleteCustomerAddressCommand(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<ChangeCustomerPasswordResponseDTO> changePassword(
            @Valid @RequestBody ChangeCustomerPasswordDTO dto) {
        ChangeCustomerPasswordCommand command = passwordDtoMapper.toCommand(dto);
        changeCustomerPasswordUseCase.execute(getCurrentCustomerId(), command);
        return ResponseEntity.ok(new ChangeCustomerPasswordResponseDTO("Password updated"));
    }
}

