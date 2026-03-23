package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService{

    private final StudentRepository studentRepository;
    private final FacultyService facultyService;

    public StudentService(StudentRepository studentRepository, FacultyService facultyService){
        this.studentRepository = studentRepository;
        this.facultyService = facultyService;
    }

    public Student createStudent( Student student) {
        return studentRepository.save( student );
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById( id );
    }

    public Student updateStudent(Student student) {
        return studentRepository.save( student );
    }

    public Student findStudent(long id) {
        return studentRepository.findById( id ).get();
    }

    public List<Student> findByAge(int age) {
        return studentRepository.findByAge( age );
    }

    public List<Student> findByAgeBetween(Integer minAge, Integer maxAge) {
        if (minAge ==  null && maxAge == null){
            return studentRepository.findAll();
        }
        if ( minAge == null){
            minAge = 0;
        }
        if ( maxAge == null){
            maxAge = 0;}
        return studentRepository.findByAgeBetween( minAge, maxAge );
    }

    public List<Student> getStudentsByFacultyName(String name) {
        return studentRepository.getStudentsByFacultyName( name );
    }

}