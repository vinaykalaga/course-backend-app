package com.course.app.repository.course;

import com.course.app.dto.CourseSummaryDto;
import com.course.app.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("""
    SELECT new com.course.app.dto.CourseSummaryDto(
        c.id,
        c.title,
        c.description,
        COUNT(e.id),
        SUM(CASE WHEN e.completed = true THEN 1 ELSE 0 END),
        SUM(CASE WHEN e.certificateGenerated = true THEN 1 ELSE 0 END)
    )
    FROM Course c
    LEFT JOIN Enrollment e ON e.course.id = c.id
    WHERE c.instructor = :instructor
    GROUP BY c.id, c.title, c.description
""")
    List<CourseSummaryDto> findDashboardCoursesByInstructor(@Param("instructor") String instructorUsername);

    //List<Course> findByInstructor(String username);
}
