package com.ute.foodiedash.application.restaurant.port;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ImageUploadPort {
    Map<String, Object> uploadImage(MultipartFile file, String folder) throws IOException;
    Map<String, Object> uploadImage(MultipartFile file) throws IOException;
}
