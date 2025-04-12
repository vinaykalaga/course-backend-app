package com.course.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class InstructorDashboardDto {
    private long totalCourses;
    private long totalLearners;
    private long certificatesIssued;
    private Map<String, Double> completionRates; // courseTitle -> %
}
