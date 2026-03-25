package com.ute.foodiedash.interfaces.rest.driver;

import com.ute.foodiedash.application.user.usecase.ApproveDriverProfileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/drivers")
@RequiredArgsConstructor
public class AdminDriverController {

    private final ApproveDriverProfileUseCase approveDriverProfileUseCase;

    @PatchMapping("/{driverId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approve(@PathVariable Long driverId) {
        approveDriverProfileUseCase.execute(driverId);
        return ResponseEntity.ok().build();
    }
}
