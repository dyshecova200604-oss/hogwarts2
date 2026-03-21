package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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

    public Student getStudentById(long studentId) {
        return null;
    }

    public Student createStudent(String name, int age) {
        return new Student();
    }

    public Student updateStudent(Long studentId, String name, int age) {
        return null;
    }

    public List<Student> getStudentsByAge(int age) {
        return new ArrayList<>();
    }

    public Faculty getFacultyByStudentId(Long studentId) {
        return null;
    }
}