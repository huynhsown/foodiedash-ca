package com.ute.foodiedash.application.user.query;

public record GoogleIdentityQueryResult(
        String sub,
        String email,
        String name,
        String picture,
        boolean emailVerified
) {
}

