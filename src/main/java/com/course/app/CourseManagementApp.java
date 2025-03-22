package com.course.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.course.app")
public class CourseManagementApp {
    public static void main(String[] args) {
        SpringApplication.run(CourseManagementApp.class, args);
    }
}
