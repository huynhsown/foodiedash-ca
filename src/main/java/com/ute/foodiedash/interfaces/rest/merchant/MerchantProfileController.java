package com.ute.foodiedash.interfaces.rest.merchant;

import com.ute.foodiedash.application.user.query.MerchantProfileQueryResult;
import com.ute.foodiedash.application.user.usecase.GetMerchantProfileUseCase;
import com.ute.foodiedash.infrastructure.security.SecurityContextHelper;
import com.ute.foodiedash.interfaces.rest.merchant.dto.MerchantProfileResponseDTO;
import com.ute.foodiedash.interfaces.rest.merchant.mapper.MerchantProfileDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MERCHANT')")
public class MerchantProfileController {

    private final GetMerchantProfileUseCase getMerchantProfileUseCase;
    private final MerchantProfileDtoMapper dtoMapper;

    @GetMapping("/api/v1/merchant/profile")
    public ResponseEntity<MerchantProfileResponseDTO> getMerchantProfile() {
        Long currentUserId = SecurityContextHelper.getCurrentUserId();
        MerchantProfileQueryResult result = getMerchantProfileUseCase.execute(currentUserId);
        MerchantProfileResponseDTO response = dtoMapper.toResponse(result);
        return ResponseEntity.ok(response);
    }
}
