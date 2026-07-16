package com.educativa.examenes.controller;

import com.educativa.examenes.dto.CalificacionDto;
import com.educativa.examenes.service.CalificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
@Tag(name = "Calificaciones", description = "Registro y consulta de notas de examenes")
public class CalificacionController {

    private final CalificacionService calificacionService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Registrar la nota de un estudiante en un examen (solo instructor). "
        + "Este endpoint es el productor del evento de la cola RabbitMQ (se conecta en el paso 3 del roadmap).")
    public ResponseEntity<CalificacionDto.Response> registrar(@Valid @RequestBody CalificacionDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calificacionService.registrar(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una calificacion por ID")
    public ResponseEntity<CalificacionDto.Response> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(calificacionService.obtenerPorId(id));
    }

    @GetMapping("/estudiante/{estudianteId}")
    @Operation(summary = "Listar las notas de un estudiante")
    public ResponseEntity<List<CalificacionDto.Response>> listarPorEstudiante(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(calificacionService.listarPorEstudiante(estudianteId));
    }

    @GetMapping("/examen/{examenId}")
    @Operation(summary = "Listar las notas registradas para un examen")
    public ResponseEntity<List<CalificacionDto.Response>> listarPorExamen(@PathVariable Long examenId) {
        return ResponseEntity.ok(calificacionService.listarPorExamen(examenId));
    }
}
