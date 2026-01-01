package com.ute.foodiedash.infrastructure.config;

import com.ute.foodiedash.infrastructure.websocket.StompPrincipalChannelInterceptor;
import com.ute.foodiedash.infrastructure.websocket.UserHandshakeHandler;
import com.ute.foodiedash.infrastructure.websocket.UserJwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final UserJwtHandshakeInterceptor userJwtHandshakeInterceptor;
    private final StompPrincipalChannelInterceptor stompPrincipalChannelInterceptor;

    @Value("${app.websocket.allowed-origins:http://localhost:5173}")
    private String[] allowedOrigins;

    @Value("${app.websocket.endpoint:/ws}")
    private String endpoint;

    @Value("${app.websocket.driver.destination-prefix:/app}")
    private String appPrefix;

    @Value("${app.websocket.broker-prefixes:/topic,/queue}")
    private String[] brokerPrefixes;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(brokerPrefixes);
        registry.setApplicationDestinationPrefixes(appPrefix);
        registry.setUserDestinationPrefix("/user");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(endpoint)
                .setAllowedOriginPatterns(allowedOrigins)
                .addInterceptors(userJwtHandshakeInterceptor)
                .setHandshakeHandler(new UserHandshakeHandler())
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompPrincipalChannelInterceptor);
    }
}
