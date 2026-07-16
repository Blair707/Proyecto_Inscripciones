package com.educativa.inscripciones.service;

import com.educativa.inscripciones.dto.InscripcionDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    public byte[] generarResumenInscripcion(InscripcionDto.Response inscripcion) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);
        document.open();

        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font fontNormal = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        Paragraph titulo = new Paragraph("Resumen de Inscripcion", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        Paragraph numeroResumen = new Paragraph("Numero de Inscripcion: " + inscripcion.getId(), fontSubtitulo);
        numeroResumen.setSpacingAfter(15);
        document.add(numeroResumen);

        document.add(new Paragraph("Datos del Estudiante", fontSubtitulo));
        document.add(new Paragraph("Nombre: " + inscripcion.getNombreEstudiante(), fontNormal));
        document.add(new Paragraph("ID Estudiante: " + inscripcion.getEstudianteId(), fontNormal));

        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Datos del Curso", fontSubtitulo));
        document.add(new Paragraph("Curso: " + inscripcion.getNombreCurso(), fontNormal));
        document.add(new Paragraph("ID Curso: " + inscripcion.getCursoId(), fontNormal));

        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Detalles de la Inscripcion", fontSubtitulo));
        document.add(new Paragraph("Estado: " + inscripcion.getEstado(), fontNormal));

        if (inscripcion.getFechaInscripcion() != null) {
            String fecha = inscripcion.getFechaInscripcion()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            document.add(new Paragraph("Fecha de Inscripcion: " + fecha, fontNormal));
        }

        document.close();
        return outputStream.toByteArray();
    }
}
