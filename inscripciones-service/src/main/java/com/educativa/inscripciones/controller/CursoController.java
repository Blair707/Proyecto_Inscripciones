package com.educativa.inscripciones.controller;

import com.educativa.inscripciones.dto.CursoDto;
import com.educativa.inscripciones.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
@Tag(name = "Cursos", description = "Gestion de cursos virtuales")
public class CursoController {

    private final CursoService cursoService;

    @PostMapping
    @Operation(summary = "Crear un nuevo curso")
    public ResponseEntity<CursoDto.Response> crear(@Valid @RequestBody CursoDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.crear(request));
    }

    @GetMapping
    @Operation(summary = "Listar todos los cursos")
    public ResponseEntity<List<CursoDto.Response>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar cursos activos")
    public ResponseEntity<List<CursoDto.Response>> listarActivos() {
        return ResponseEntity.ok(cursoService.listarActivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener curso por ID")
    public ResponseEntity<CursoDto.Response> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un curso")
    public ResponseEntity<CursoDto.Response> actualizar(@PathVariable Long id, @Valid @RequestBody CursoDto.Request request) {
        return ResponseEntity.ok(cursoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un curso")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
