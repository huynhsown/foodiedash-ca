package com.ute.foodiedash.interfaces.rest.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Long userId;
    private String email;
    private String fullName;
    private String avatarUrl;
    private List<String> roles;
    private List<String> permissions;
}
