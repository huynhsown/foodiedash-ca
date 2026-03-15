package com.ute.foodiedash.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQProperties {

    private String exchange;

    private String routingKeyPattern;

    private RoutingKeys routingKeys;

    private Queues queues;


    @Getter
    @Setter
    public static class RoutingKeys {
        private String notificationCreated;
    }

    @Getter
    @Setter
    public static class Queues {
        private String notification;
    }
}
