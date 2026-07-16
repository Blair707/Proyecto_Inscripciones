package com.educativa.examenes.service;

import com.educativa.examenes.dto.ExamenDto;
import com.educativa.examenes.exception.ResourceNotFoundException;
import com.educativa.examenes.model.Examen;
import com.educativa.examenes.repository.ExamenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamenService {

    private final ExamenRepository examenRepository;

    public ExamenDto.Response crear(ExamenDto.Request request) {
        Examen examen = Examen.builder()
            .cursoId(request.getCursoId())
            .titulo(request.getTitulo())
            .fecha(request.getFecha())
            .ponderacion(request.getPonderacion())
            .build();

        return toResponse(examenRepository.save(examen));
    }

    public List<ExamenDto.Response> listar() {
        return examenRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ExamenDto.Response obtenerPorId(Long id) {
        return toResponse(findExamenOrThrow(id));
    }

    public List<ExamenDto.Response> listarPorCurso(Long cursoId) {
        return examenRepository.findByCursoId(cursoId).stream()
            .map(this::toResponse).collect(Collectors.toList());
    }

    public void eliminar(Long id) {
        if (!examenRepository.existsById(id)) {
            throw new ResourceNotFoundException("Examen no encontrado con id: " + id);
        }
        examenRepository.deleteById(id);
    }

    public Examen findExamenOrThrow(Long id) {
        return examenRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Examen no encontrado con id: " + id));
    }

    private ExamenDto.Response toResponse(Examen e) {
        return ExamenDto.Response.builder()
            .id(e.getId())
            .cursoId(e.getCursoId())
            .titulo(e.getTitulo())
            .fecha(e.getFecha())
            .ponderacion(e.getPonderacion())
            .build();
    }
}
