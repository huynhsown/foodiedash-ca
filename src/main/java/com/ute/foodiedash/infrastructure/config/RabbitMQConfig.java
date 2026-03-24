package com.ute.foodiedash.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
@EnableConfigurationProperties(RabbitMQProperties.class)
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final RabbitMQProperties properties;

    @Bean
    TopicExchange notificationExchange() {
        return new TopicExchange(properties.getExchange());
    }

    @Bean
    TopicExchange domainExchange() {
        return new TopicExchange(properties.getDomainExchange());
    }

    @Bean
    TopicExchange deadLetterExchange() {
        return new TopicExchange(properties.getDomainDeadLetterExchange());
    }

    @Bean
    Queue notificationQueue() {
        return QueueBuilder
                .durable(properties.getQueues().getNotification())
                .build();
    }

    @Bean
    Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(properties.getRoutingKeyPattern());
    }

    @Bean
    Queue restaurantCreatedQueue() {
        return QueueBuilder
                .durable(properties.getDomainQueues().getRestaurantCreated())
                .deadLetterExchange(properties.getDomainDeadLetterExchange())
                .deadLetterRoutingKey(properties.getDomainRoutingKeys().getRestaurantCreated())
                .build();
    }

    @Bean
    Queue restaurantUpdatedQueue() {
        return QueueBuilder
                .durable(properties.getDomainQueues().getRestaurantUpdated())
                .deadLetterExchange(properties.getDomainDeadLetterExchange())
                .deadLetterRoutingKey(properties.getDomainRoutingKeys().getRestaurantUpdated())
                .build();
    }

    @Bean
    Queue restaurantDeletedQueue() {
        return QueueBuilder
                .durable(properties.getDomainQueues().getRestaurantDeleted())
                .deadLetterExchange(properties.getDomainDeadLetterExchange())
                .deadLetterRoutingKey(properties.getDomainRoutingKeys().getRestaurantDeleted())
                .build();
    }

    @Bean
    Queue orderReadyQueue() {
        return QueueBuilder
                .durable(properties.getDomainQueues().getOrderReady())
                .deadLetterExchange(properties.getDomainDeadLetterExchange())
                .deadLetterRoutingKey(properties.getDomainRoutingKeys().getOrderReady())
                .build();
    }

    @Bean
    Queue restaurantCreatedDlq() {
        return QueueBuilder
                .durable(properties.getDomainDlqQueues().getRestaurantCreated())
                .build();
    }

    @Bean
    Queue restaurantUpdatedDlq() {
        return QueueBuilder
                .durable(properties.getDomainDlqQueues().getRestaurantUpdated())
                .build();
    }

    @Bean
    Queue restaurantDeletedDlq() {
        return QueueBuilder
                .durable(properties.getDomainDlqQueues().getRestaurantDeleted())
                .build();
    }

    @Bean
    Queue orderReadyDlq() {
        return QueueBuilder
                .durable(properties.getDomainDlqQueues().getOrderReady())
                .build();
    }

    @Bean
    Binding restaurantCreatedBinding(Queue restaurantCreatedQueue, TopicExchange domainExchange) {
        return BindingBuilder
                .bind(restaurantCreatedQueue)
                .to(domainExchange)
                .with(properties.getDomainRoutingKeys().getRestaurantCreated());
    }

    @Bean
    Binding restaurantUpdatedBinding(Queue restaurantUpdatedQueue, TopicExchange domainExchange) {
        return BindingBuilder
                .bind(restaurantUpdatedQueue)
                .to(domainExchange)
                .with(properties.getDomainRoutingKeys().getRestaurantUpdated());
    }

    @Bean
    Binding restaurantDeletedBinding(Queue restaurantDeletedQueue, TopicExchange domainExchange) {
        return BindingBuilder
                .bind(restaurantDeletedQueue)
                .to(domainExchange)
                .with(properties.getDomainRoutingKeys().getRestaurantDeleted());
    }

    @Bean
    Binding orderReadyBinding(Queue orderReadyQueue, TopicExchange domainExchange) {
        return BindingBuilder
                .bind(orderReadyQueue)
                .to(domainExchange)
                .with(properties.getDomainRoutingKeys().getOrderReady());
    }

    @Bean
    Binding restaurantCreatedDlqBinding(Queue restaurantCreatedDlq, TopicExchange deadLetterExchange) {
        return BindingBuilder
                .bind(restaurantCreatedDlq)
                .to(deadLetterExchange)
                .with(properties.getDomainRoutingKeys().getRestaurantCreated());
    }

    @Bean
    Binding restaurantUpdatedDlqBinding(Queue restaurantUpdatedDlq, TopicExchange deadLetterExchange) {
        return BindingBuilder
                .bind(restaurantUpdatedDlq)
                .to(deadLetterExchange)
                .with(properties.getDomainRoutingKeys().getRestaurantUpdated());
    }

    @Bean
    Binding restaurantDeletedDlqBinding(Queue restaurantDeletedDlq, TopicExchange deadLetterExchange) {
        return BindingBuilder
                .bind(restaurantDeletedDlq)
                .to(deadLetterExchange)
                .with(properties.getDomainRoutingKeys().getRestaurantDeleted());
    }

    @Bean
    Binding orderReadyDlqBinding(Queue orderReadyDlq, TopicExchange deadLetterExchange) {
        return BindingBuilder
                .bind(orderReadyDlq)
                .to(deadLetterExchange)
                .with(properties.getDomainRoutingKeys().getOrderReady());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
