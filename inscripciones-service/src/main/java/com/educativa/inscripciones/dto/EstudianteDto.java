package com.educativa.inscripciones.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class EstudianteDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        private String nombre;
        @NotBlank
        private String apellido;
        @NotBlank @Email
        private String email;
        @NotBlank
        private String telefono;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String nombre;
        private String apellido;
        private String email;
        private String telefono;
    }
}
