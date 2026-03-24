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

    private String domainExchange;

    private DomainRoutingKeys domainRoutingKeys;

    private DomainQueues domainQueues;

    private String domainDeadLetterExchange;

    private DomainDlqQueues domainDlqQueues;


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

    @Getter
    @Setter
    public static class DomainRoutingKeys {
        private String restaurantCreated;
        private String restaurantUpdated;
        private String restaurantDeleted;
        private String orderReady;
    }

    @Getter
    @Setter
    public static class DomainQueues {
        private String restaurantCreated;
        private String restaurantUpdated;
        private String restaurantDeleted;
        private String orderReady;
    }

    @Getter
    @Setter
    public static class DomainDlqQueues {
        private String restaurantCreated;
        private String restaurantUpdated;
        private String restaurantDeleted;
        private String orderReady;
    }
}
