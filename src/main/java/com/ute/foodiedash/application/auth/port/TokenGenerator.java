package com.ute.foodiedash.application.auth.port;

import java.util.List;
import java.util.Map;

public interface TokenGenerator {
    String generateToken(Long userId, String email, List<String> roles);
    boolean validateToken(String token);
    Map<String, Object> extractClaims(String token);
}
