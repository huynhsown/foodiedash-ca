package com.ute.foodiedash.application.user.query;

import java.util.List;

public record LoginQueryResult(
        String token,
        Long userId,
        String email,
        String fullName,
        String avatarUrl,
        List<String> roles,
        List<String> permissions
) {}
