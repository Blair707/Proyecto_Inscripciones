package com.educativa.bff.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.cola-calificaciones}")
    private String colaCalificaciones;

    @Value("${app.rabbitmq.routing-key-calificaciones}")
    private String routingKeyCalificaciones;

    @Bean
    public TopicExchange cursosExchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public Queue colaCalificacionesQueue() {
        return new Queue(colaCalificaciones, true);
    }

    @Bean
    public Binding calificacionesBinding(Queue colaCalificacionesQueue, TopicExchange cursosExchange) {
        return BindingBuilder.bind(colaCalificacionesQueue).to(cursosExchange).with(routingKeyCalificaciones);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}