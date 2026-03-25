package ru.hogwarts.school.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController("/student-query")
public class StudentQueryController {

    private final StudentService studentService;

    public StudentQueryController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/count")
    public Long getStudentCount() {
        return studentService.countAllStudents();
    }
    @GetMapping("/average-age")
    public Integer getAverageAge() {
        return studentService.averageAgeOfStudents();
    }

    @GetMapping("/last-students")
    public List<Student> getFiveLastStudents() {
        return studentService.getFiveLastStudents();
    }
}