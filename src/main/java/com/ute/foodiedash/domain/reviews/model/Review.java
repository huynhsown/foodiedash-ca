package com.ute.foodiedash.domain.reviews.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.reviews.enums.ReviewStatus;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Review extends BaseEntity {
    private Long id;
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private Integer rating;
    private String comment;
    private List<String> images;
    private String merchantReply;
    private LocalDateTime repliedAt;
    private ReviewStatus status;

    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    public static Review create(
            Long customerId,
            Long orderId,
            Long restaurantId,
            Integer rating,
            String comment,
            List<String> images
    ) {
        if (customerId == null) {
            throw new BadRequestException("Customer id required");
        }
        if (orderId == null && restaurantId == null) {
            throw new BadRequestException("Either order id or restaurant id required");
        }
        if (rating == null) {
            throw new BadRequestException("Rating required");
        }
        if (rating < MIN_RATING || rating > MAX_RATING) {
            throw new BadRequestException("Rating must be between " + MIN_RATING + " and " + MAX_RATING);
        }
        if (comment == null || comment.isBlank()) {
            throw new BadRequestException("Comment required");
        }

        Review review = new Review();
        review.customerId = customerId;
        review.orderId = orderId;
        review.restaurantId = restaurantId;
        review.rating = rating;
        review.comment = comment;
        review.images = images != null ? new ArrayList<>(images) : new ArrayList<>();
        review.status = ReviewStatus.ACTIVE;
        return review;
    }

    public static Review reconstruct(
            Long id,
            Long customerId,
            Long orderId,
            Long restaurantId,
            Integer rating,
            String comment,
            List<String> images,
            String merchantReply,
            LocalDateTime repliedAt,
            ReviewStatus status,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        Review review = new Review();
        review.id = id;
        review.customerId = customerId;
        review.orderId = orderId;
        review.restaurantId = restaurantId;
        review.rating = rating;
        review.comment = comment;
        review.images = images != null ? new ArrayList<>(images) : new ArrayList<>();
        review.merchantReply = merchantReply;
        review.repliedAt = repliedAt;
        review.status = status;
        review.restoreAudit(createdAt, updatedAt, createdBy, updatedBy, deletedAt, version);
        return review;
    }

    public void reply(String reply) {
        if (reply == null || reply.isBlank()) {
            throw new BadRequestException("Reply content required");
        }
        this.merchantReply = reply;
        this.repliedAt = LocalDateTime.now();
    }

    public void hide() {
        if (status == ReviewStatus.HIDDEN) {
            return;
        }
        this.status = ReviewStatus.HIDDEN;
    }

    public void show() {
        if (status == ReviewStatus.ACTIVE) {
            return;
        }
        this.status = ReviewStatus.ACTIVE;
    }

    public void report() {
        if (status == ReviewStatus.REPORTED) {
            return;
        }
        this.status = ReviewStatus.REPORTED;
    }

    public void update(Integer rating, String comment, List<String> images) {
        if (!canUpdate()) {
            throw new BadRequestException("Review can only be updated within 24 hours of creation");
        }
        if (rating == null) {
            throw new BadRequestException("Rating required");
        }
        if (rating < MIN_RATING || rating > MAX_RATING) {
            throw new BadRequestException("Rating must be between " + MIN_RATING + " and " + MAX_RATING);
        }
        if (comment == null || comment.isBlank()) {
            throw new BadRequestException("Comment required");
        }
        this.rating = rating;
        this.comment = comment;
        this.images = images != null ? new ArrayList<>(images) : new ArrayList<>();
    }

    public boolean canUpdate() {
        if (getCreatedAt() == null) {
            return false;
        }
        return ChronoUnit.HOURS.between(getCreatedAt(), Instant.now()) < 24;
    }

    public boolean isActive() {
        return status == ReviewStatus.ACTIVE;
    }

    public boolean isHidden() {
        return status == ReviewStatus.HIDDEN;
    }

    public boolean isReported() {
        return status == ReviewStatus.REPORTED;
    }

    public boolean hasReply() {
        return merchantReply != null && !merchantReply.isBlank();
    }

    public void validateOwnership(Long customerId) {
        if (!this.customerId.equals(customerId)) {
            throw new IllegalStateException("Not your review");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
