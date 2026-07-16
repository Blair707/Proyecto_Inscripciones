package com.educativa.examenes.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;

public class CalificacionDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private Long examenId;

        @NotNull
        private Long estudianteId;

        @NotNull
        @DecimalMin("1.0")
        @DecimalMax("7.0")
        private Double nota;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long examenId;
        private String tituloExamen;
        private Long cursoId;
        private Long estudianteId;
        private Double nota;
        private java.time.LocalDateTime fechaRegistro;
    }

    /**
     * Evento que llega por RabbitMQ desde bff-service. Mismo "shape" que
     * CalificacionMensajeDto.Evento en bff-service; se mapean por un
     * classMapper compartido (ver RabbitMQConfig) en vez de compartir la
     * clase Java entre microservicios.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Evento implements Serializable {
        private Long examenId;
        private Long estudianteId;
        private Double nota;
        private String registradoPor;
    }
}
