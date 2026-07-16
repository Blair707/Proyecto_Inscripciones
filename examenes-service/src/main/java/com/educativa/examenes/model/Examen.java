package com.educativa.examenes.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "examenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Examen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia al curso del inscripciones-service. No es FK física porque
    // cada microservicio es dueño de su propia base de datos.
    @Column(nullable = false)
    private Long cursoId;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private LocalDate fecha;

    // Porcentaje que vale este examen sobre la nota final del curso (0-100)
    @Column(nullable = false)
    private Integer ponderacion;
}
