package com.ute.foodiedash.interfaces.rest.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyToReviewRequestDTO {

    @NotBlank(message = "Reply content is required")
    private String reply;
}
