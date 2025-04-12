package com.course.app.repository.course;

import com.course.app.model.course.Enrollment;
import com.course.app.model.auth.User;
import com.course.app.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUsername(String username);
    Optional<Enrollment> findByUsernameAndCourse(String username, Course course);
    //Optional<Enrollment> findByUserAndCourse(User user, Course course);
    Optional<Enrollment> findByUsernameAndCourseId(String username, Long courseId);

}
