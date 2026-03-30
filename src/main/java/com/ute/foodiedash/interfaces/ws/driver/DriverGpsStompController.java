package com.ute.foodiedash.interfaces.ws.driver;

import com.ute.foodiedash.application.driver.usecase.RecordDriverGpsHeartbeatUseCase;
import com.ute.foodiedash.infrastructure.websocket.DriverWebSocketPrincipal;
import com.ute.foodiedash.interfaces.ws.driver.dto.DriverGpsHeartbeatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class DriverGpsStompController {

    private final RecordDriverGpsHeartbeatUseCase recordDriverGpsHeartbeatUseCase;

    @MessageMapping("/driver/gps/heartbeat")
    public void heartbeat(@Payload DriverGpsHeartbeatMessage body, Principal principal) {
        if (!(principal instanceof DriverWebSocketPrincipal(Long userId))) {
            return;
        }
        if (body.lat() == null || body.lng() == null) {
            return;
        }
        recordDriverGpsHeartbeatUseCase.execute(
                userId,
                body.lat(),
                body.lng(),
                body.ts()
        );
    }
}