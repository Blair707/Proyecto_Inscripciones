package com.educativa.examenes.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

public class ExamenDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private Long cursoId;

        @NotBlank
        private String titulo;

        @NotNull
        private LocalDate fecha;

        @NotNull
        @Min(1)
        @Max(100)
        private Integer ponderacion;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long cursoId;
        private String titulo;
        private LocalDate fecha;
        private Integer ponderacion;
    }
}
