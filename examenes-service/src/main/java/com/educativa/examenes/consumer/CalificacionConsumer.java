package com.educativa.examenes.consumer;

import com.educativa.examenes.dto.CalificacionDto;
import com.educativa.examenes.service.CalificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor de RabbitMQ: procesa los eventos de calificacion publicados por
 * bff-service (productor) y los persiste usando la misma logica de negocio
 * que el endpoint sincrono POST /api/calificaciones.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CalificacionConsumer {

    private final CalificacionService calificacionService;

    @RabbitListener(queues = "${app.rabbitmq.cola-calificaciones}")
    public void procesar(CalificacionDto.Evento evento) {
        log.info("Evento de calificacion recibido: examenId={}, estudianteId={}, registradoPor={}",
            evento.getExamenId(), evento.getEstudianteId(), evento.getRegistradoPor());

        CalificacionDto.Request request = CalificacionDto.Request.builder()
            .examenId(evento.getExamenId())
            .estudianteId(evento.getEstudianteId())
            .nota(evento.getNota())
            .build();

        try {
            calificacionService.registrar(request);
            log.info("Calificacion persistida correctamente desde la cola (examenId={}, estudianteId={})",
                evento.getExamenId(), evento.getEstudianteId());
        } catch (Exception e) {
            // No relanzamos la excepcion para no dejar el mensaje en loop de
            // reintento infinito (ej: nota duplicada). Queda registrado en logs.
            log.error("No se pudo persistir la calificacion recibida por cola: {}", e.getMessage());
        }
    }
}
