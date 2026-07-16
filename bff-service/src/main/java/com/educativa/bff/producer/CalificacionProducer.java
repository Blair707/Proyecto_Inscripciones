package com.educativa.bff.producer;

import com.educativa.bff.dto.CalificacionMensajeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalificacionProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key-calificaciones}")
    private String routingKey;

    public void publicar(CalificacionMensajeDto.Evento evento) {
        rabbitTemplate.convertAndSend(exchange, routingKey, evento);
    }
}
