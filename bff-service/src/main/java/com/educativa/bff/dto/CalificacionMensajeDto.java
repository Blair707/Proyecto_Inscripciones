package com.educativa.bff.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;

public class CalificacionMensajeDto {

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

    /**
     * Payload que efectivamente se serializa y viaja por RabbitMQ.
     * Serializable por buena práctica, aunque el converter usado es JSON.
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
