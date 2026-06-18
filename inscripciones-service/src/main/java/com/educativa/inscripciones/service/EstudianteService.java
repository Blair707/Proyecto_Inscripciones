package com.educativa.inscripciones.service;

import com.educativa.inscripciones.dto.EstudianteDto;
import com.educativa.inscripciones.exception.BusinessException;
import com.educativa.inscripciones.exception.ResourceNotFoundException;
import com.educativa.inscripciones.model.Estudiante;
import com.educativa.inscripciones.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteDto.Response crear(EstudianteDto.Request request) {
        if (estudianteRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Ya existe un estudiante con el email: " + request.getEmail());
        }
        Estudiante estudiante = Estudiante.builder()
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .email(request.getEmail())
            .telefono(request.getTelefono())
            .build();
        return toResponse(estudianteRepository.save(estudiante));
    }

    public List<EstudianteDto.Response> listar() {
        return estudianteRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public EstudianteDto.Response obtenerPorId(Long id) {
        return toResponse(estudianteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id)));
    }

    public EstudianteDto.Response actualizar(Long id, EstudianteDto.Request request) {
        Estudiante estudiante = estudianteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));
        if (!estudiante.getEmail().equals(request.getEmail()) && estudianteRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Ya existe un estudiante con el email: " + request.getEmail());
        }
        estudiante.setNombre(request.getNombre());
        estudiante.setApellido(request.getApellido());
        estudiante.setEmail(request.getEmail());
        estudiante.setTelefono(request.getTelefono());
        return toResponse(estudianteRepository.save(estudiante));
    }

    public void eliminar(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estudiante no encontrado con id: " + id);
        }
        estudianteRepository.deleteById(id);
    }

    private EstudianteDto.Response toResponse(Estudiante e) {
        return EstudianteDto.Response.builder()
            .id(e.getId())
            .nombre(e.getNombre())
            .apellido(e.getApellido())
            .email(e.getEmail())
            .telefono(e.getTelefono())
            .build();
    }
}
