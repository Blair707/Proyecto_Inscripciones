package com.educativa.inscripciones.controller;

import com.educativa.inscripciones.dto.InscripcionDto;
import com.educativa.inscripciones.service.InscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
@Tag(name = "Inscripciones", description = "Gestion de inscripciones a cursos")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @PostMapping
    @Operation(summary = "Crear una nueva inscripcion")
    public ResponseEntity<InscripcionDto.Response> crear(@Valid @RequestBody InscripcionDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionService.crear(request));
    }

    @GetMapping
    @Operation(summary = "Listar todas las inscripciones")
    public ResponseEntity<List<InscripcionDto.Response>> listar() {
        return ResponseEntity.ok(inscripcionService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener inscripcion por ID")
    public ResponseEntity<InscripcionDto.Response> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.obtenerPorId(id));
    }

    @GetMapping("/estudiante/{estudianteId}")
    @Operation(summary = "Listar inscripciones por estudiante")
    public ResponseEntity<List<InscripcionDto.Response>> listarPorEstudiante(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(inscripcionService.listarPorEstudiante(estudianteId));
    }

    @GetMapping("/curso/{cursoId}")
    @Operation(summary = "Listar inscripciones por curso")
    public ResponseEntity<List<InscripcionDto.Response>> listarPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(inscripcionService.listarPorCurso(cursoId));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una inscripcion")
    public ResponseEntity<InscripcionDto.Response> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.cancelar(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una inscripcion")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inscripcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
