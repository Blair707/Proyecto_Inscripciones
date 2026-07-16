package com.educativa.examenes.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calificaciones", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"examen_id", "estudiante_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examen_id", nullable = false)
    private Examen examen;

    // Referencia al estudiante del inscripciones-service (sin FK física).
    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    // Escala chilena: 1.0 a 7.0
    @Column(nullable = false)
    private Double nota;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;
}
