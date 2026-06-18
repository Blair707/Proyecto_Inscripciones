package com.educativa.inscripciones.repository;

import com.educativa.inscripciones.model.Inscripcion;
import com.educativa.inscripciones.model.Inscripcion.EstadoInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByEstudianteId(Long estudianteId);
    List<Inscripcion> findByCursoId(Long cursoId);
    List<Inscripcion> findByEstado(EstadoInscripcion estado);
    boolean existsByEstudianteIdAndCursoIdAndEstado(Long estudianteId, Long cursoId, EstadoInscripcion estado);
    long countByCursoIdAndEstado(Long cursoId, EstadoInscripcion estado);
}
