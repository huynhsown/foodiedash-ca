package com.ute.foodiedash.infrastructure.cloudinary.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ute.foodiedash.infrastructure.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "cloudinary", name = "cloud-name")
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;
    
    @Value("${cloudinary.folder:}")
    private String defaultFolder;

    @Override
    public Map<String, Object> uploadImage(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        Map<String, Object> params = new HashMap<>();
        if (folder != null && !folder.isEmpty()) {
            params.put("folder", folder);
        }
        params.put("resource_type", "image");
        params.put("overwrite", true);

        return cloudinary.uploader().upload(file.getBytes(), params);
    }

    @Override
    public Map<String, Object> uploadImage(MultipartFile file) throws IOException {
        return uploadImage(file, defaultFolder);
    }

    @Override
    public Map<String, Object> deleteImage(String publicId) throws IOException {
        if (publicId == null || publicId.isEmpty()) {
            throw new IllegalArgumentException("Public ID cannot be null or empty");
        }

        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    @Override
    public String getImageUrl(String publicId) {
        if (publicId == null || publicId.isEmpty()) {
            return null;
        }

        return cloudinary.url().generate(publicId);
    }
}
