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

import java.util.List;

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

    // 🔐 GET /courses/my-courses
    /*@GetMapping("/my-courses")
    public ResponseEntity<List<Course>> getMyCourses(Authentication auth) {
        String username = auth.getName(); // from JWT
        List<Course> courses = enrollmentService.getCoursesByUsername(username);
        return ResponseEntity.ok(courses);
    }*/

    @GetMapping("/status/{courseId}")
    public ResponseEntity<Enrollment> getStatus(@PathVariable Long courseId, Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.of(enrollmentService.findByUsernameAndCourse(username, courseId));
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

