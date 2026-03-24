package com.ute.foodiedash.interfaces.rest.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewResponseDTO {

    private Long id;
    private Long orderId;
    private Long customerId;
    private Long restaurantId;
    private Integer rating;
    private String comment;
    private List<String> images;
    private String merchantReply;
    private LocalDateTime repliedAt;
    private String status;
    private Instant createdAt;
}
