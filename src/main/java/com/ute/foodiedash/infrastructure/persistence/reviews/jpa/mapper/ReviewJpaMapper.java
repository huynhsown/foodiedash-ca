package com.ute.foodiedash.infrastructure.persistence.reviews.jpa.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ute.foodiedash.domain.reviews.model.Review;
import com.ute.foodiedash.infrastructure.persistence.reviews.jpa.entity.ReviewJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewJpaMapper {
    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    default Review toDomain(ReviewJpaEntity e) {
        if (e == null) {
            return null;
        }

        List<String> images = parseImages(e.getImages());
        return Review.reconstruct(
                e.getId(),
                e.getCustomerId(),
                e.getOrderId(),
                e.getRestaurantId(),
                e.getRating(),
                e.getComment(),
                images,
                e.getMerchantReply(),
                e.getRepliedAt(),
                e.getStatus(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getCreatedBy(),
                e.getUpdatedBy(),
                e.getDeletedAt(),
                e.getVersion()
        );
    }

    default void updateEntity(@MappingTarget ReviewJpaEntity e, Review domain) {
        if (domain == null) {
            return;
        }
        e.setCustomerId(domain.getCustomerId());
        e.setOrderId(domain.getOrderId());
        e.setRestaurantId(domain.getRestaurantId());
        e.setRating(domain.getRating());
        e.setComment(domain.getComment());
        e.setImages(serializeImages(domain.getImages()));
        e.setMerchantReply(domain.getMerchantReply());
        e.setRepliedAt(domain.getRepliedAt());
        e.setStatus(domain.getStatus());
        e.setUpdatedAt(domain.getUpdatedAt());
        e.setUpdatedBy(domain.getUpdatedBy());
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
    }

    default ReviewJpaEntity toJpaEntity(Review domain) {
        if (domain == null) {
            return null;
        }

        ReviewJpaEntity e = new ReviewJpaEntity();
        e.setId(domain.getId());
        e.setCustomerId(domain.getCustomerId());
        e.setOrderId(domain.getOrderId());
        e.setRestaurantId(domain.getRestaurantId());
        e.setRating(domain.getRating());
        e.setComment(domain.getComment());
        e.setImages(serializeImages(domain.getImages()));
        e.setMerchantReply(domain.getMerchantReply());
        e.setRepliedAt(domain.getRepliedAt());
        e.setStatus(domain.getStatus());

        e.setCreatedAt(domain.getCreatedAt());
        e.setUpdatedAt(domain.getUpdatedAt());
        e.setCreatedBy(domain.getCreatedBy());
        e.setUpdatedBy(domain.getUpdatedBy());
        e.setDeletedAt(domain.getDeletedAt());
        e.setVersion(domain.getVersion());
        return e;
    }

    default List<String> parseImages(String imagesJson) {
        if (imagesJson == null || imagesJson.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return OBJECT_MAPPER.readValue(imagesJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Cannot parse review images", ex);
        }
    }

    default String serializeImages(List<String> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(images);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Cannot serialize review images", ex);
        }
    }
}
