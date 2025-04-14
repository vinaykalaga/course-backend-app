package com.course.app.dto;

public record PopularCourseDto(
        Long id,
        String title,
        String description,
        long enrollmentCount
) {}