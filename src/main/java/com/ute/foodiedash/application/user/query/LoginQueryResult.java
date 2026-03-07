package com.ute.foodiedash.application.user.query;

import java.util.List;

public record LoginQueryResult(
        String token,
        Long userId,
        String email,
        String fullName,
        List<String> roles
) {}
