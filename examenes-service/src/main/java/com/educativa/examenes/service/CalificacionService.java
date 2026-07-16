package com.educativa.examenes.service;

import com.educativa.examenes.dto.CalificacionDto;
import com.educativa.examenes.exception.BusinessException;
import com.educativa.examenes.exception.ResourceNotFoundException;
import com.educativa.examenes.model.Calificacion;
import com.educativa.examenes.model.Examen;
import com.educativa.examenes.repository.CalificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final ExamenService examenService;

    public CalificacionDto.Response registrar(CalificacionDto.Request request) {
        Examen examen = examenService.findExamenOrThrow(request.getExamenId());

        if (calificacionRepository.existsByExamenIdAndEstudianteId(request.getExamenId(), request.getEstudianteId())) {
            throw new BusinessException("El estudiante ya tiene una nota registrada para este examen");
        }

        Calificacion calificacion = Calificacion.builder()
            .examen(examen)
            .estudianteId(request.getEstudianteId())
            .nota(request.getNota())
            .fechaRegistro(LocalDateTime.now())
            .build();

        Calificacion guardada = calificacionRepository.save(calificacion);

        // Nota: este metodo es usado tanto por el endpoint sincrono
        // POST /api/calificaciones como por CalificacionConsumer, que lo
        // invoca cuando llega un evento desde la cola RabbitMQ (publicado
        // por bff-service). Misma logica de negocio para ambos caminos.

        return toResponse(guardada);
    }

    public List<CalificacionDto.Response> listarPorEstudiante(Long estudianteId) {
        return calificacionRepository.findByEstudianteId(estudianteId).stream()
            .map(this::toResponse).collect(Collectors.toList());
    }

    public List<CalificacionDto.Response> listarPorExamen(Long examenId) {
        return calificacionRepository.findByExamenId(examenId).stream()
            .map(this::toResponse).collect(Collectors.toList());
    }

    public CalificacionDto.Response obtenerPorId(Long id) {
        return toResponse(calificacionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Calificacion no encontrada con id: " + id)));
    }

    private CalificacionDto.Response toResponse(Calificacion c) {
        return CalificacionDto.Response.builder()
            .id(c.getId())
            .examenId(c.getExamen().getId())
            .tituloExamen(c.getExamen().getTitulo())
            .cursoId(c.getExamen().getCursoId())
            .estudianteId(c.getEstudianteId())
            .nota(c.getNota())
            .fechaRegistro(c.getFechaRegistro())
            .build();
    }
}
