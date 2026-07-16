package com.educativa.inscripciones.controller;

import com.educativa.inscripciones.dto.InscripcionDto;
import com.educativa.inscripciones.service.InscripcionService;
import com.educativa.inscripciones.service.PdfService;
import com.educativa.inscripciones.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inscripciones/{id}/resumen")
@RequiredArgsConstructor
@Tag(name = "Resumen de Inscripcion", description = "Generacion y gestion de resumenes en PDF y S3")
public class ResumenController {

    private final InscripcionService inscripcionService;
    private final PdfService pdfService;
    private final S3Service s3Service;

    @GetMapping("/descargar")
    @Operation(summary = "Generar y descargar el resumen PDF de una inscripcion")
    public ResponseEntity<byte[]> descargarResumen(@PathVariable Long id) throws Exception {
        InscripcionDto.Response inscripcion = inscripcionService.obtenerPorId(id);
        byte[] pdf = pdfService.generarResumenInscripcion(inscripcion);

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=resumen-inscripcion-" + id + ".pdf")
            .body(pdf);
    }

    @PostMapping("/s3")
    @Operation(summary = "Subir el resumen PDF de una inscripcion a AWS S3")
    public ResponseEntity<String> subirResumenS3(@PathVariable Long id) throws Exception {
        InscripcionDto.Response inscripcion = inscripcionService.obtenerPorId(id);
        byte[] pdf = pdfService.generarResumenInscripcion(inscripcion);
        String key = s3Service.subirResumen(id, pdf);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Resumen subido exitosamente a S3: " + key);
    }

    @GetMapping("/s3")
    @Operation(summary = "Descargar el resumen PDF desde AWS S3")
    public ResponseEntity<byte[]> descargarResumenS3(@PathVariable Long id) throws Exception {
        byte[] pdf = s3Service.descargarResumen(id);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=resumen-inscripcion-" + id + ".pdf")
            .body(pdf);
    }

    @PutMapping("/s3")
    @Operation(summary = "Actualizar el resumen PDF en AWS S3")
    public ResponseEntity<String> actualizarResumenS3(@PathVariable Long id) throws Exception {
        InscripcionDto.Response inscripcion = inscripcionService.obtenerPorId(id);
        byte[] pdf = pdfService.generarResumenInscripcion(inscripcion);
        String key = s3Service.actualizarResumen(id, pdf);
        return ResponseEntity.ok("Resumen actualizado en S3: " + key);
    }

    @DeleteMapping("/s3")
    @Operation(summary = "Eliminar el resumen PDF de AWS S3")
    public ResponseEntity<Void> eliminarResumenS3(@PathVariable Long id) {
        s3Service.eliminarResumen(id);
        return ResponseEntity.noContent().build();
    }
}
