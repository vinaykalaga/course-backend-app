package com.course.app.service;

import com.course.app.model.course.Course;
import com.course.app.repository.course.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    public Optional<Course> getCourseDetailById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        return course;
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course updated) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setInstructor(updated.getInstructor());
        existing.setLevel(updated.getLevel());
        existing.setDurationWeeks(updated.getDurationWeeks());
        return courseRepository.save(existing);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
