package com.ute.foodiedash.interfaces.rest.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginDTO {
    @NotBlank(message = "Google idToken is required")
    private String idToken;
}

