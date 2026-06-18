package com.educativa.inscripciones.service;

import com.educativa.inscripciones.dto.InscripcionDto;
import com.educativa.inscripciones.exception.BusinessException;
import com.educativa.inscripciones.exception.ResourceNotFoundException;
import com.educativa.inscripciones.model.Curso;
import com.educativa.inscripciones.model.Estudiante;
import com.educativa.inscripciones.model.Inscripcion;
import com.educativa.inscripciones.model.Inscripcion.EstadoInscripcion;
import com.educativa.inscripciones.repository.CursoRepository;
import com.educativa.inscripciones.repository.EstudianteRepository;
import com.educativa.inscripciones.repository.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;

    public InscripcionDto.Response crear(InscripcionDto.Request request) {
        Estudiante estudiante = estudianteRepository.findById(request.getEstudianteId())
            .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + request.getEstudianteId()));

        Curso curso = cursoRepository.findById(request.getCursoId())
            .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + request.getCursoId()));

        if (!curso.getActivo()) {
            throw new BusinessException("El curso no está activo");
        }

        if (inscripcionRepository.existsByEstudianteIdAndCursoIdAndEstado(
                request.getEstudianteId(), request.getCursoId(), EstadoInscripcion.ACTIVA)) {
            throw new BusinessException("El estudiante ya está inscrito en este curso");
        }

        long inscritos = inscripcionRepository.countByCursoIdAndEstado(request.getCursoId(), EstadoInscripcion.ACTIVA);
        if (inscritos >= curso.getCapacidad()) {
            throw new BusinessException("El curso ha alcanzado su capacidad máxima");
        }

        Inscripcion inscripcion = Inscripcion.builder()
            .estudiante(estudiante)
            .curso(curso)
            .fechaInscripcion(LocalDateTime.now())
            .estado(EstadoInscripcion.ACTIVA)
            .build();

        return toResponse(inscripcionRepository.save(inscripcion));
    }

    public List<InscripcionDto.Response> listar() {
        return inscripcionRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public InscripcionDto.Response obtenerPorId(Long id) {
        return toResponse(inscripcionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inscripcion no encontrada con id: " + id)));
    }

    public List<InscripcionDto.Response> listarPorEstudiante(Long estudianteId) {
        return inscripcionRepository.findByEstudianteId(estudianteId).stream()
            .map(this::toResponse).collect(Collectors.toList());
    }

    public List<InscripcionDto.Response> listarPorCurso(Long cursoId) {
        return inscripcionRepository.findByCursoId(cursoId).stream()
            .map(this::toResponse).collect(Collectors.toList());
    }

    public InscripcionDto.Response cancelar(Long id) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inscripcion no encontrada con id: " + id));
        if (inscripcion.getEstado() != EstadoInscripcion.ACTIVA) {
            throw new BusinessException("Solo se pueden cancelar inscripciones activas");
        }
        inscripcion.setEstado(EstadoInscripcion.CANCELADA);
        return toResponse(inscripcionRepository.save(inscripcion));
    }

    public void eliminar(Long id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inscripcion no encontrada con id: " + id);
        }
        inscripcionRepository.deleteById(id);
    }

    private InscripcionDto.Response toResponse(Inscripcion i) {
        return InscripcionDto.Response.builder()
            .id(i.getId())
            .estudianteId(i.getEstudiante().getId())
            .nombreEstudiante(i.getEstudiante().getNombre() + " " + i.getEstudiante().getApellido())
            .cursoId(i.getCurso().getId())
            .nombreCurso(i.getCurso().getNombre())
            .fechaInscripcion(i.getFechaInscripcion())
            .estado(i.getEstado())
            .build();
    }
}
