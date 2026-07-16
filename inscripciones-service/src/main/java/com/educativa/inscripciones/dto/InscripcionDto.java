package com.educativa.inscripciones.dto;

import com.educativa.inscripciones.model.Inscripcion.EstadoInscripcion;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

public class InscripcionDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private Long estudianteId;
        @NotNull
        private Long cursoId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long estudianteId;
        private String nombreEstudiante;
        private Long cursoId;
        private String nombreCurso;
        private LocalDateTime fechaInscripcion;
        private EstadoInscripcion estado;
    }
}
