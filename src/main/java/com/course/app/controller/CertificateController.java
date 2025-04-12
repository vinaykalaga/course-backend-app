package com.course.app.controller;

import com.course.app.service.CertificateGenerator;
import com.course.app.service.EnrollmentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/certificate")
public class CertificateController {

    private final EnrollmentService enrollmentService;
    private final CertificateGenerator certificateGenerator;

    public CertificateController(EnrollmentService enrollmentService, CertificateGenerator certificateGenerator) {
        this.enrollmentService = enrollmentService;
        this.certificateGenerator = certificateGenerator;
    }

    // 🔐 GET /certificate/download/{courseId}
    @GetMapping("/download/{courseId}")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long courseId, Authentication auth) throws IOException {
        String username = auth.getName();
        ByteArrayOutputStream pdfStream = certificateGenerator.generateCertificate(username, courseId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("certificate_" + username + "_" + courseId + ".pdf")
                .build());

        return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
    }
}
