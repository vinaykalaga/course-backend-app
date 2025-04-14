package com.course.app.model.course;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private String instructor;
    private String level;        // e.g., Beginner, Intermediate, Advanced
    private int durationWeeks;   // e.g., 4 (weeks)
    @Lob
    @Column(columnDefinition = "TEXT") // for larger content
    private String content; // ✅ Course material content

}
