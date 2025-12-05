package com.ute.foodiedash.interfaces.rest.restaurant;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantImageCommand;
import com.ute.foodiedash.application.restaurant.query.RestaurantImageQueryResult;
import com.ute.foodiedash.application.restaurant.usecase.CreateRestaurantImageUseCase;
import com.ute.foodiedash.application.restaurant.usecase.RestoreRestaurantImageUseCase;
import com.ute.foodiedash.application.restaurant.usecase.SoftDeleteRestaurantImageUseCase;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.CreateRestaurantImageDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.dto.RestaurantImageResponseDTO;
import com.ute.foodiedash.interfaces.rest.restaurant.mapper.RestaurantImageDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/restaurant-images")
@RequiredArgsConstructor
public class RestaurantImageController {

    private final CreateRestaurantImageUseCase createRestaurantImageUseCase;
    private final SoftDeleteRestaurantImageUseCase softDeleteRestaurantImageUseCase;
    private final RestoreRestaurantImageUseCase restoreRestaurantImageUseCase;
    private final RestaurantImageDtoMapper dtoMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestaurantImageResponseDTO> createRestaurantImage(
            @Valid @ModelAttribute CreateRestaurantImageDTO createRestaurantImageDTO,
            @RequestPart(value = "image", required = true) MultipartFile image) {
        CreateRestaurantImageCommand command = dtoMapper.toCommand(createRestaurantImageDTO);
        RestaurantImageQueryResult result = createRestaurantImageUseCase.execute(command, image);
        RestaurantImageResponseDTO response = dtoMapper.toResponseDto(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantImage(@PathVariable Long id) {
        softDeleteRestaurantImageUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRestaurantImage(@PathVariable Long id) {
        restoreRestaurantImageUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
