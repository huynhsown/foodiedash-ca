package com.ute.foodiedash.interfaces.rest.driver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDriverIdentityDTO {

    @NotBlank(message = "ID card number is required")
    @Size(max = 50, message = "ID card number must not exceed 50 characters")
    private String idCardNumber;

    @NotBlank(message = "ID card front image is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String idCardFrontUrl;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String idCardBackUrl;
}
