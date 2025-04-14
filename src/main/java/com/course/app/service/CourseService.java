package com.course.app.service;

import com.course.app.dto.PopularCourseDto;
import com.course.app.model.course.Course;
import com.course.app.repository.course.CourseRepository;
import com.course.app.repository.course.EnrollmentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepo;

    public CourseService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepo) {
        this.courseRepository = courseRepository;
        this.enrollmentRepo=enrollmentRepo;
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

    //public void deleteCourse(Long id) {
      //  courseRepository.deleteById(id);
   // }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        enrollmentRepo.deleteByCourse(course); // step 1: delete dependent enrollments
        courseRepository.delete(course);             // step 2: delete course itself
    }

    public List<PopularCourseDto> getPopularCourses() {
        Pageable limit = PageRequest.of(0, 5);
        return courseRepository.findTopPopularCourses(limit);
    }


}
