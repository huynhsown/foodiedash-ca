package com.ute.foodiedash.interfaces.rest.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterMerchantDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(max = 255, message = "Full name must not exceed 255 characters")
    private String fullName;

    @NotBlank(message = "Business name is required")
    @Size(max = 255, message = "Business name must not exceed 255 characters")
    private String businessName;

    @Email(message = "Contact email must be valid")
    @Size(max = 255, message = "Contact email must not exceed 255 characters")
    private String contactEmail;

    @Size(max = 20, message = "Contact phone must not exceed 20 characters")
    private String contactPhone;
}
