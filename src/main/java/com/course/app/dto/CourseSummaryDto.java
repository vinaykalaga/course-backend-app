package com.course.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSummaryDto {
    private Long id;
    private String title;
    private String description;
    private Long  enrollmentCount;
    private long completedCount; // ✅ New
    private long certificateCount;

}

