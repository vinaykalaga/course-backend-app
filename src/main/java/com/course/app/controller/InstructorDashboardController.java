package com.course.app.controller;

import com.course.app.dto.CourseSummaryDto;
import com.course.app.dto.InstructorDashboardDto;
import com.course.app.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<List<CourseSummaryDto>> getInstructorCourses(Authentication authentication) {
        String username = authentication.getName(); // Authenticated instructor
        List<CourseSummaryDto> courses = dashboardService.getCoursesByInstructor(username);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/dashboard/export/{courseId}")
    public ResponseEntity<Resource> exportLearners(@PathVariable Long courseId) {
        String csv = dashboardService.exportCourseLearners(courseId);
        ByteArrayResource resource = new ByteArrayResource(csv.getBytes(StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=learners_course_" + courseId + ".csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

}