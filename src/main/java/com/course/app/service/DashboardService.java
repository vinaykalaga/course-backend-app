package com.course.app.service;

import com.course.app.dto.CourseSummaryDto;
import com.course.app.dto.InstructorDashboardDto;
import com.course.app.model.auth.User;
import com.course.app.model.course.Course;
import com.course.app.model.course.Enrollment;
import com.course.app.repository.auth.UserRepository;
import com.course.app.repository.course.CourseRepository;
import com.course.app.repository.course.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public List<CourseSummaryDto> getCoursesByInstructor(String instructorUsername) {
        return courseRepository.findDashboardCoursesByInstructor(instructorUsername);
    }


        public String exportCourseLearners(Long courseId) {
            List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

            StringBuilder csv = new StringBuilder("Learner,Email,Completed\n");

            for (Enrollment e : enrollments) {
                String username = e.getUsername();
                Optional<User> userOpt = userRepository.findByUsername(username);

                if (userOpt.isPresent()) {
                    User learner = userOpt.get();
                    csv.append(learner.getUsername()).append(",")

                            .append(e.isCompleted() ? "Yes" : "No").append("\n");
                } else {
                    csv.append(username).append(",N/A,")
                            .append(e.isCompleted() ? "Yes" : "No").append("\n");
                }
            }

            return csv.toString();
        }



}
