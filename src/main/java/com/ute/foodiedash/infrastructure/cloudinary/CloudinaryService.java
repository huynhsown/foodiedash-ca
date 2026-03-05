package com.ute.foodiedash.infrastructure.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    /**
     * Upload image to Cloudinary
     * @param file MultipartFile to upload
     * @param folder Folder path in Cloudinary
     * @return Map containing upload result with public_id and url
     * @throws IOException if upload fails
     */
    Map<String, Object> uploadImage(MultipartFile file, String folder) throws IOException;

    /**
     * Upload image to Cloudinary with default folder
     * @param file MultipartFile to upload
     * @return Map containing upload result with public_id and url
     * @throws IOException if upload fails
     */
    Map<String, Object> uploadImage(MultipartFile file) throws IOException;

    /**
     * Delete image from Cloudinary
     * @param publicId Public ID of the image to delete
     * @return Map containing deletion result
     * @throws IOException if deletion fails
     */
    Map<String, Object> deleteImage(String publicId) throws IOException;

    /**
     * Get image URL from public ID
     * @param publicId Public ID of the image
     * @return Image URL
     */
    String getImageUrl(String publicId);
}
