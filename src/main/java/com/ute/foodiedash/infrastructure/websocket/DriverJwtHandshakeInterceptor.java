package com.ute.foodiedash.infrastructure.websocket;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverJwtHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return false;
        }
        HttpServletRequest http = servletRequest.getServletRequest();
        String token = http.getParameter("token");
        if (token == null || token.isBlank()) {
            log.warn("WebSocket handshake rejected: missing token");
            return false;
        }
        try {
            Long driverUserId = 1L;
            attributes.put("PRINCIPAL", new DriverWebSocketPrincipal(driverUserId));
            log.info("WebSocket handshake accepted: driverUserId={}", driverUserId);
        } catch (Exception e) {
            log.warn("WebSocket handshake rejected: {}", e.toString());
            return false;
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {
        if (exception != null) {
            log.warn("WebSocket handshake completed with error: {}", exception.toString());
        }
    }
}
