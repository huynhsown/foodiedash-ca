package com.ute.foodiedash.infrastructure.cloudinary;

import com.ute.foodiedash.application.menu.port.ImageUploadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MenuItemImageUploadAdapter implements ImageUploadPort {
    private final CloudinaryService cloudinaryService;

    @Override
    public Map<String, Object> uploadImage(MultipartFile file, String folder) throws IOException {
        return cloudinaryService.uploadImage(file, folder);
    }

    @Override
    public Map<String, Object> uploadImage(MultipartFile file) throws IOException {
        return cloudinaryService.uploadImage(file);
    }
}
