package com.educativa.inscripciones.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class CursoDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        private String nombre;
        @NotBlank
        private String descripcion;
        @NotBlank
        private String instructor;
        @NotNull @Min(1)
        private Integer capacidad;
        @NotNull
        private Boolean activo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String nombre;
        private String descripcion;
        private String instructor;
        private Integer capacidad;
        private Boolean activo;
    }
}
