package com.ute.foodiedash.infrastructure.security;

import com.ute.foodiedash.domain.common.exception.UnauthorizedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SecurityContextHelper {

    private static boolean isAnonymousOrMissing(Authentication authentication) {
        return authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken;
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAnonymousOrMissing(authentication)) {
            throw new UnauthorizedException("Authentication required");
        }

        String userIdStr = authentication.getName();
        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("Invalid user ID in token");
        }
    }

    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAnonymousOrMissing(authentication)) {
            throw new UnauthorizedException("Authentication required");
        }

        Object details = authentication.getDetails();
        if (details instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> detailsMap = (Map<String, Object>) details;
            Object email = detailsMap.get("email");
            if (email != null) {
                return email.toString();
            }
        }

        throw new UnauthorizedException("Email not found in authentication context");
    }

    public static List<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAnonymousOrMissing(authentication)) {
            throw new UnauthorizedException("Authentication required");
        }

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.replace("ROLE_", ""))
                .collect(Collectors.toList());
    }
}
