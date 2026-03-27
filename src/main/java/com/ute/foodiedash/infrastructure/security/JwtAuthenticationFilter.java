package com.ute.foodiedash.infrastructure.security;

import com.ute.foodiedash.application.auth.port.TokenGenerator;
import io.jsonwebtoken.ExpiredJwtException;
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

        filterChain.doFilter(request, response);
        return;

//        String path = request.getRequestURI();
//
//        // Skip filter for public endpoints
//        if (path.startsWith("/api/v1/auth/") || path.startsWith("/api/v1/users/register/")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = extractTokenFromRequest(request);
//
//        if (token == null) {
//            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
//            return;
//        }
//
//        try {
//            if (!tokenGenerator.validateToken(token)) {
//                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
//                return;
//            }
//
//            Map<String, Object> claims = tokenGenerator.extractClaims(token);
//            String userId = (String) claims.get("sub");
//            String email = (String) claims.get("email");
//            @SuppressWarnings("unchecked")
//            List<String> roles = (List<String>) claims.get("roles");
//
//            List<SimpleGrantedAuthority> authorities = roles.stream()
//                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                    .collect(Collectors.toList());
//
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    userId,
//                    null,
//                    authorities
//            );
//
//            // Store additional user info in details
//            authentication.setDetails(Map.of("email", email, "userId", Long.parseLong(userId)));
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            filterChain.doFilter(request, response);
//        } catch (ExpiredJwtException e) {
//            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
//        } catch (Exception e) {
//            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
//        }
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
