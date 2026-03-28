package com.ute.foodiedash.infrastructure.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompPrincipalChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }
        Object attr = accessor.getSessionAttributes() != null
                ? accessor.getSessionAttributes().get("PRINCIPAL")
                : null;
        if (attr instanceof Principal principal) {
            accessor.setUser(principal);
            log.info(
                    "STOMP CONNECT: sessionId={}, user={}",
                    accessor.getSessionId(),
                    principal.getName());
        } else {
            log.warn("STOMP CONNECT: sessionId={}, no PRINCIPAL in session attributes", accessor.getSessionId());
        }
        return message;
    }
}
