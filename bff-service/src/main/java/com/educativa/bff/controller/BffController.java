package com.educativa.bff.controller;

import com.educativa.bff.dto.CalificacionMensajeDto;
import com.educativa.bff.producer.CalificacionProducer;
import com.educativa.bff.security.JwtClaimsExtractor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * BFF (Backend for Frontend): orquesta las llamadas a las colas de RabbitMQ.
 * No persiste datos directamente; publica eventos que examenes-service
 * consume de forma asíncrona (ver CalificacionConsumer en ese microservicio).
 */
@RestController
@RequestMapping("/bff/calificaciones")
@RequiredArgsConstructor
@Tag(name = "BFF Calificaciones", description = "Orquesta el registro asincrono de calificaciones via RabbitMQ")
public class BffController {

    private final CalificacionProducer calificacionProducer;
    private final JwtClaimsExtractor jwtClaimsExtractor;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Publica un evento de calificacion en la cola (productor). "
        + "examenes-service lo consume de forma asincrona y persiste la nota.")
    public ResponseEntity<Map<String, String>> registrarCalificacion(@Valid @RequestBody CalificacionMensajeDto.Request request) {
        CalificacionMensajeDto.Evento evento = CalificacionMensajeDto.Evento.builder()
            .examenId(request.getExamenId())
            .estudianteId(request.getEstudianteId())
            .nota(request.getNota())
            .registradoPor(jwtClaimsExtractor.getSub())
            .build();

        calificacionProducer.publicar(evento);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(Map.of("status", "encolado", "mensaje", "La calificacion fue publicada en la cola y se procesara de forma asincrona"));
    }
}
