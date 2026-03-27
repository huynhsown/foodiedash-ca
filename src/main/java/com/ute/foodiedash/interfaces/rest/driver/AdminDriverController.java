package com.ute.foodiedash.interfaces.rest.driver;

import com.ute.foodiedash.application.user.usecase.ApproveDriverProfileUseCase;
import com.ute.foodiedash.application.user.usecase.SearchDriversUseCase;
import com.ute.foodiedash.domain.common.model.PageResult;
import com.ute.foodiedash.application.user.query.SearchDriverQueryResult;
import com.ute.foodiedash.interfaces.rest.common.dto.PageInfo;
import com.ute.foodiedash.interfaces.rest.driver.dto.SearchDriverResponseDTO;
import com.ute.foodiedash.interfaces.rest.driver.dto.SearchDriversRequestDTO;
import com.ute.foodiedash.interfaces.rest.driver.mapper.DriverDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/drivers")
@RequiredArgsConstructor
public class AdminDriverController {

    private final ApproveDriverProfileUseCase approveDriverProfileUseCase;
    private final SearchDriversUseCase searchDriversUseCase;
    private final DriverDtoMapper driverDtoMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageInfo<SearchDriverResponseDTO>> search(
            @Valid @ModelAttribute SearchDriversRequestDTO request) {
        var command = driverDtoMapper.toCommand(request);
        PageResult<SearchDriverQueryResult> result = searchDriversUseCase.execute(command);
        List<SearchDriverResponseDTO> content = result.getContent().stream()
                .map(driverDtoMapper::toResponseDto)
                .toList();
        PageInfo<SearchDriverResponseDTO> page = new PageInfo<>(
                content, result.getPage(), result.getSize(),
                result.getTotalElements(), result.getTotalPages(),
                content.isEmpty(), content.size(),
                result.hasNext(), result.hasPrevious()
        );
        return ResponseEntity.ok(page);
    }

    @PatchMapping("/{driverId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approve(@PathVariable Long driverId) {
        approveDriverProfileUseCase.execute(driverId);
        return ResponseEntity.ok().build();
    }
}
