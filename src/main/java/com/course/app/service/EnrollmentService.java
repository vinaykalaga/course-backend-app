package com.course.app.service;

import com.course.app.model.auth.User;
import com.course.app.model.course.Course;
import com.course.app.dto.CourseProgressDto;
import com.course.app.model.course.Enrollment;
import com.course.app.repository.auth.UserRepository;
import com.course.app.repository.course.CourseRepository;
import com.course.app.repository.course.EnrollmentRepository;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepo;
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;

    public EnrollmentService(EnrollmentRepository enrollmentRepo, CourseRepository courseRepo, UserRepository userRepo) {
        this.enrollmentRepo = enrollmentRepo;
        this.courseRepo = courseRepo;
        this.userRepo=userRepo;
    }

    public String enrollUserToCourse(String username, Long courseId) {
        Optional<Course> courseOpt = courseRepo.findById(courseId);
        if (courseOpt.isEmpty()) return "Invalid course";

        Course course = courseOpt.get();

        boolean alreadyEnrolled = enrollmentRepo.findByUsernameAndCourse(username, course).isPresent();
        if (alreadyEnrolled) return "Already enrolled";

        Enrollment enrollment = new Enrollment();
        enrollment.setUsername(username);
        enrollment.setCourse(course);

        enrollmentRepo.save(enrollment);
        return "Enrolled successfully";
    }

    public List<Course> getCoursesByUsername(String username) {
        List<Enrollment> enrollments = enrollmentRepo.findByUsername(username);
        return enrollments.stream().map(Enrollment::getCourse).toList();
    }

    public boolean isCourseCompleted(String username, Long courseId) {
        Optional<Enrollment> enrollment = enrollmentRepo.findByUsernameAndCourseId(username, courseId);
        return enrollment.map(Enrollment::isCompleted).orElse(false);
    }

    public String markCourseCompleted(String username, Long courseId) {
        Optional<Enrollment> enrollment = enrollmentRepo.findByUsernameAndCourseId(username, courseId);
        if (enrollment.isPresent()) {
            enrollment.get().setCompleted(true);
            enrollmentRepo.save(enrollment.get());
            return "Course marked as completed";
        }
        return "Not enrolled in course";
    }


    public Optional<Enrollment> findByUsernameAndCourse(String username, Long courseId) {
        return enrollmentRepo.findByUsernameAndCourseId(username,courseId);
    }

    public List<CourseProgressDto> getCourseProgressForUser(String username) {
        List<Enrollment> enrollments = enrollmentRepo.findByUsername(username);

        return enrollments.stream()
                .map(enroll -> {
                    Course course = enroll.getCourse();
                    return new CourseProgressDto(
                            course.getId(),
                            course.getTitle(),
                            course.getDescription(),
                            course.getInstructor(),
                            enroll.isCompleted()
                    );
                })
                .collect(Collectors.toList());
    }


    public Enrollment findByCourseIdAndUsername(Long id, String name) {
        return enrollmentRepo.findByCourseIdAndUsername(id,name);
    }
}
