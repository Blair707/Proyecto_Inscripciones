package com.educativa.examenes.repository;

import com.educativa.examenes.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByEstudianteId(Long estudianteId);
    List<Calificacion> findByExamenId(Long examenId);
    Optional<Calificacion> findByExamenIdAndEstudianteId(Long examenId, Long estudianteId);
    boolean existsByExamenIdAndEstudianteId(Long examenId, Long estudianteId);
}
