package com.course.app.dto;

public class CourseProgressDto {

    private Long id;
    private String title;
    private String description;
    private String instructor;
    private boolean completed;

    public CourseProgressDto() {
    }

    public CourseProgressDto(Long id, String title, String description, String instructor, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructor = instructor;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
