package com.course.app.service;

import com.course.app.model.course.Course;
import com.course.app.model.course.Enrollment;
import com.course.app.repository.course.CourseRepository;
import com.course.app.repository.course.EnrollmentRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class CertificateGenerator {

    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;

    public CertificateGenerator(CourseRepository courseRepo, EnrollmentRepository enrollmentRepo) {
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
    }

    public ByteArrayOutputStream generateCertificate(String username, Long courseId) throws IOException {
        Optional<Course> courseOpt = courseRepo.findById(courseId);

        if (courseOpt.isEmpty()) {
            throw new IllegalArgumentException("Course not found");
        }

        Course course = courseOpt.get();
        Optional<Enrollment> enrollmentOpt = enrollmentRepo.findByUsernameAndCourse(username, course);

        if (enrollmentOpt.isEmpty() || !enrollmentOpt.get().isCompleted()) {
            throw new AccessDeniedException("Course not completed or not enrolled");
        }

        // 📝 Create PDF
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream stream = new PDPageContentStream(document, page);
        stream.beginText();
        stream.setFont(PDType1Font.HELVETICA_BOLD, 24);
        stream.newLineAtOffset(100, 700);
        stream.showText("Course Completion Certificate");
        stream.endText();

        stream.beginText();
        stream.setFont(PDType1Font.HELVETICA, 16);
        stream.newLineAtOffset(100, 650);
        stream.showText("This is to certify that " + username);
        stream.endText();

        stream.beginText();
        stream.setFont(PDType1Font.HELVETICA, 16);
        stream.newLineAtOffset(100, 620);
        stream.showText("has successfully completed the course:");
        stream.endText();

        stream.beginText();
        stream.setFont(PDType1Font.HELVETICA_BOLD, 18);
        stream.newLineAtOffset(100, 590);
        stream.showText("\"" + course.getTitle() + "\"");
        stream.endText();

        stream.beginText();
        stream.setFont(PDType1Font.HELVETICA_OBLIQUE, 14);
        stream.newLineAtOffset(100, 550);
        stream.showText("Date: " + LocalDate.now());
        stream.endText();

        stream.close();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        document.save(output);
        document.close();

        return output;
    }
}
