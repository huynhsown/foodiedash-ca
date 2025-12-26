package com.ute.foodiedash.interfaces.rest.user.dto;

import com.ute.foodiedash.domain.user.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileResponseDTO {
    private Long id;
    private String email;
    private String phone;
    private String fullName;
    private String avatarUrl;
    private Instant createdAt;
    private Instant dateOfBirth;
    private Gender gender;
}

