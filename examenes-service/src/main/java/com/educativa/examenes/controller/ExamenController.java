package com.educativa.examenes.controller;

import com.educativa.examenes.dto.ExamenDto;
import com.educativa.examenes.service.ExamenService;
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
@RequestMapping("/api/examenes")
@RequiredArgsConstructor
@Tag(name = "Examenes", description = "Gestion de examenes por curso")
public class ExamenController {

    private final ExamenService examenService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Crear un examen para un curso (solo instructor)")
    public ResponseEntity<ExamenDto.Response> crear(@Valid @RequestBody ExamenDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examenService.crear(request));
    }

    @GetMapping
    @Operation(summary = "Listar todos los examenes")
    public ResponseEntity<List<ExamenDto.Response>> listar() {
        return ResponseEntity.ok(examenService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener examen por ID")
    public ResponseEntity<ExamenDto.Response> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(examenService.obtenerPorId(id));
    }

    @GetMapping("/curso/{cursoId}")
    @Operation(summary = "Listar examenes de un curso")
    public ResponseEntity<List<ExamenDto.Response>> listarPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(examenService.listarPorCurso(cursoId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Eliminar un examen (solo instructor)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        examenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
