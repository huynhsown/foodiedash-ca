package com.ute.foodiedash.application.restaurant.usecase;

import com.ute.foodiedash.application.restaurant.command.CreateRestaurantImageCommand;
import com.ute.foodiedash.application.restaurant.port.ImageUploadPort;
import com.ute.foodiedash.application.restaurant.query.RestaurantImageQueryResult;
import com.ute.foodiedash.domain.common.exception.NotFoundException;
import com.ute.foodiedash.domain.restaurant.model.RestaurantImage;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantImageRepository;
import com.ute.foodiedash.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateRestaurantImageUseCase {
    private final RestaurantImageRepository restaurantImageRepository;
    private final RestaurantRepository restaurantRepository;
    private final ImageUploadPort imageUploadPort;

    @Transactional
    public RestaurantImageQueryResult execute(CreateRestaurantImageCommand command, MultipartFile image) {
        if (!restaurantRepository.existsById(command.restaurantId())) {
            throw new NotFoundException("Restaurant not found with id " + command.restaurantId());
        }
        if (image == null || image.isEmpty()) {
            throw new NotFoundException("Image is required");
        }

        RestaurantImage restaurantImage = new RestaurantImage();
        restaurantImage.setRestaurantId(command.restaurantId());

        try {
            Map<String, Object> uploadResult = imageUploadPort.uploadImage(image, "restaurant-images");
            String imageUrl = uploadResult.get("secure_url").toString();
            restaurantImage.setImageUrl(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload restaurant image", e);
        }

        RestaurantImage saved = restaurantImageRepository.save(restaurantImage);

        return new RestaurantImageQueryResult(
            saved.getId(),
            saved.getRestaurantId(),
            saved.getImageUrl()
        );
    }
}
