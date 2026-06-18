package com.educativa.inscripciones.repository;

import com.educativa.inscripciones.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByActivo(Boolean activo);
    List<Curso> findByInstructor(String instructor);
}
