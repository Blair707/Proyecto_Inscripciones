package com.educativa.inscripciones.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String subirResumen(Long inscripcionId, byte[] contenido) {
        String carpeta = String.valueOf(inscripcionId);
        String key = carpeta + "/resumen-inscripcion-" + inscripcionId + ".pdf";

        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/pdf")
                .build(),
            RequestBody.fromBytes(contenido)
        );

        return key;
    }

    public byte[] descargarResumen(Long inscripcionId) throws IOException {
        String key = inscripcionId + "/resumen-inscripcion-" + inscripcionId + ".pdf";

        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
            GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()
        );

        return response.readAllBytes();
    }

    public String actualizarResumen(Long inscripcionId, byte[] contenido) {
        return subirResumen(inscripcionId, contenido);
    }

    public void eliminarResumen(Long inscripcionId) {
        String key = inscripcionId + "/resumen-inscripcion-" + inscripcionId + ".pdf";

        s3Client.deleteObject(
            DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()
        );
    }
}
