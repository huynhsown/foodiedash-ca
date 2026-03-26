package com.ute.foodiedash.interfaces.rest.user.dto;

import com.ute.foodiedash.domain.user.enums.Gender;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UpdateCustomerProfileDTO {

    @Size(max = 255, message = "Full name must not exceed 255 characters")
    private String fullName;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
    private String avatarUrl;

    @Past(message = "Date of birth must be in the past")
    private Instant dateOfBirth;

    private Gender gender;
}

