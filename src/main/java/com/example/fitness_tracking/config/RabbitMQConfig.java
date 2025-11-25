package com.example.fitness_tracking.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitMQConfig {

    @Value("${fitness.exchange.name:fitness-exchange}")
    private String exchangeName;

    @Value("${fitness.queue.name:fitness-events}")
    private String queueName;

    @Value("${fitness.dlq.name:fitness-events-dlq}")
    private String dlqName;

    @Value("${fitness.routing.key:fitness.event}")
    private String routingKey;

    @Value("${fitness.dlq.routing.key:fitness.event.dlq}")
    private String dlqRoutingKey;

    @Bean
    public TopicExchange fitnessExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue fitnessQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", exchangeName)
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey)
                .build();
    }

    @Bean
    public Queue fitnessDLQ() {
        return QueueBuilder.durable(dlqName).build();
    }

    @Bean
    public Binding fitnessBinding(Queue fitnessQueue, TopicExchange fitnessExchange) {
        return BindingBuilder.bind(fitnessQueue)
                .to(fitnessExchange)
                .with(routingKey);
    }

    @Bean
    public Binding fitnessDLQBinding(Queue fitnessDLQ, TopicExchange fitnessExchange) {
        return BindingBuilder.bind(fitnessDLQ)
                .to(fitnessExchange)
                .with(dlqRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public RetryOperationsInterceptor retryInterceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(1000, 2.0, 10000)
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setAdviceChain(retryInterceptor());
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}