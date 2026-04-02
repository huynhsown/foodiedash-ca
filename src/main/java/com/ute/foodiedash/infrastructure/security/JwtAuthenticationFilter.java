package com.ute.foodiedash.infrastructure.security;

import com.ute.foodiedash.application.auth.port.TokenGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenGenerator tokenGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractTokenFromRequest(request);
        if (token == null || token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (!tokenGenerator.validateToken(token)) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }

            Map<String, Object> claims = tokenGenerator.extractClaims(token);
            String userId = claims.get("sub") != null ? claims.get("sub").toString() : null;
            if (userId == null || userId.isBlank()) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token subject");
                return;
            }

            String email = claims.get("email") != null ? claims.get("email").toString() : null;
            List<SimpleGrantedAuthority> authorities =
                    new ArrayList<>(mapPermissionsToAuthorities(claims.get("permissions")));
//            authorities.addAll(mapRolesToAuthorities(claims.get("roles")));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    authorities
            );
            authentication.setDetails(Map.of(
                    "email", email != null ? email : "",
                    "userId", Long.parseLong(userId)
            ));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (NumberFormatException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid user id in token");
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        }
    }

    private static List<SimpleGrantedAuthority> mapRolesToAuthorities(Object rolesClaim) {
        if (rolesClaim == null) {
            return List.of();
        }
        if (rolesClaim instanceof List<?> list) {
            return list.stream()
                    .filter(o -> o != null && !o.toString().isBlank())
                    .map(o -> new SimpleGrantedAuthority("ROLE_" + o.toString().trim()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return List.of();
    }

    private static List<SimpleGrantedAuthority> mapPermissionsToAuthorities(Object permissionsClaim) {
        if (permissionsClaim == null) {
            return List.of();
        }
        if (permissionsClaim instanceof List<?> list) {
            return list.stream()
                    .filter(o -> o != null && !o.toString().isBlank())
                    .map(o -> new SimpleGrantedAuthority(o.toString().trim()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return List.of();
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\":" + status + ",\"message\":\"" + message + "\"}");
    }
}
