package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService{

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public Student createStudent( Student student) {
        logger.info("Was invoked method createStudent {}", student.getName());
        return studentRepository.save( student );
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method deleteStudent {}", id);
        studentRepository.deleteById( id );
    }

    public Student updateStudent(Student student) {
        logger.info("Was invoked method updateStudent {}", student.getName());
        return studentRepository.save( student );
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method findStudent {}", id);
        return studentRepository.findById( id ).get();
    }

    public List<Student> findByAge(int age) {
        logger.info("Was invoked method findByAge {}", age);
        return studentRepository.findByAge( age );
    }

    public List<Student> findByAgeBetween(Integer minAge, Integer maxAge) {
        logger.info("Was invoked method findByAgeBetween {} and {}", minAge, maxAge);
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
        logger.info("Was invoked method getStudentsByFacultyName {}", name);
        return studentRepository.getStudentsByFacultyName( name );
    }

    public Long countAllStudents() {
        logger.info("Was invoked method countAllStudents");
        return studentRepository.countAllStudents();
    }

    public Integer averageAgeOfStudents() {
        logger.info("Was invoked method averageAgeOfStudents");
        return studentRepository.averageAgeOfStudents();
    }

    public List<Student> getFiveLastStudents() {
        logger.info("Was invoked method getFiveLastStudents");
        return studentRepository.getFiveLastStudents();
    }

}