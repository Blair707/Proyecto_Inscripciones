package com.educativa.examenes.repository;

import com.educativa.examenes.model.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamenRepository extends JpaRepository<Examen, Long> {
    List<Examen> findByCursoId(Long cursoId);
}
