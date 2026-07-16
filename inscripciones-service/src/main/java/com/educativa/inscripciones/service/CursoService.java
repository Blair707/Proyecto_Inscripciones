package com.educativa.inscripciones.service;

import com.educativa.inscripciones.dto.CursoDto;
import com.educativa.inscripciones.exception.ResourceNotFoundException;
import com.educativa.inscripciones.model.Curso;
import com.educativa.inscripciones.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoDto.Response crear(CursoDto.Request request) {
        Curso curso = Curso.builder()
            .nombre(request.getNombre())
            .descripcion(request.getDescripcion())
            .instructor(request.getInstructor())
            .capacidad(request.getCapacidad())
            .activo(request.getActivo())
            .build();
        return toResponse(cursoRepository.save(curso));
    }

    public List<CursoDto.Response> listar() {
        return cursoRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<CursoDto.Response> listarActivos() {
        return cursoRepository.findByActivo(true).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CursoDto.Response obtenerPorId(Long id) {
        return toResponse(cursoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + id)));
    }

    public CursoDto.Response actualizar(Long id, CursoDto.Request request) {
        Curso curso = cursoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + id));
        curso.setNombre(request.getNombre());
        curso.setDescripcion(request.getDescripcion());
        curso.setInstructor(request.getInstructor());
        curso.setCapacidad(request.getCapacidad());
        curso.setActivo(request.getActivo());
        return toResponse(cursoRepository.save(curso));
    }

    public void eliminar(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curso no encontrado con id: " + id);
        }
        cursoRepository.deleteById(id);
    }

    private CursoDto.Response toResponse(Curso curso) {
        return CursoDto.Response.builder()
            .id(curso.getId())
            .nombre(curso.getNombre())
            .descripcion(curso.getDescripcion())
            .instructor(curso.getInstructor())
            .capacidad(curso.getCapacidad())
            .activo(curso.getActivo())
            .build();
    }
}
