package com.course.app.controller;

import com.course.app.dto.PopularCourseDto;
import com.course.app.model.course.Course;
import com.course.app.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/getCourse")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/getCourse/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Optional<Course> course= courseService.getCourseDetailById(id);
       return course.map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/addCourse")
    public ResponseEntity<Course> addCourse(@RequestBody Course course, Authentication auth) {
        String username = auth.getName(); // ⬅️ get logged-in instructor
        course.setInstructor(username);   // ⬅️ must be saved to match dashboard query
        return ResponseEntity.ok(courseService.createCourse(course));
    }

    @PutMapping("/updateCourse/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        return ResponseEntity.ok(courseService.updateCourse(id, updatedCourse));
    }

    @DeleteMapping("/deleteCourse/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PopularCourseDto>> getPopularCourses() {
        return ResponseEntity.ok(courseService.getPopularCourses());
    }

}
