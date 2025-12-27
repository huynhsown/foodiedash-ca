package com.ute.foodiedash.interfaces.rest.user.dto;

import com.ute.foodiedash.domain.user.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String email;
    private String phone;
    private String fullName;
    private String avatarUrl;
    private UserStatus status;
    private Instant createdAt;
}
