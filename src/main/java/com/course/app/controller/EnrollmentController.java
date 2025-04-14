package com.course.app.controller;

import com.course.app.dto.CourseProgressDto;
import com.course.app.model.course.Course;
import com.course.app.model.course.Enrollment;
import com.course.app.service.EnrollmentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // 🔐 POST /courses/enroll/5
    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<String> enroll(@PathVariable Long courseId, Authentication auth) {
        String username = auth.getName(); // from JWT
        String result = enrollmentService.enrollUserToCourse(username, courseId);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/status/{id}")
    public ResponseEntity<?> getCourseStatus(@PathVariable Long id, Principal principal) {
        // Check if the user is enrolled and completed the course
        Enrollment enrollment = enrollmentService.findByCourseIdAndUsername(id, principal.getName());
        if (enrollment == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> status = new HashMap<>();
        status.put("completed", enrollment.isCompleted());
        return ResponseEntity.ok(status);
    }

    @PostMapping("/complete/{courseId}")
    public ResponseEntity<String> markCompleted(@PathVariable Long courseId, Authentication auth) {
        String username = auth.getName();
        String status = enrollmentService.markCourseCompleted(username, courseId);
        return ResponseEntity.ok(status);
    }

    /*@GetMapping("/certificate/{courseId}")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long courseId, Authentication auth) {
        String username = auth.getName();
        byte[] pdfData = enrollmentService.generateCertificate(username, courseId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"certificate.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
    }*/

    @GetMapping("/my-courses")
    public ResponseEntity<List<CourseProgressDto>> getMyCourses(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(enrollmentService.getCourseProgressForUser(username));
    }

}

