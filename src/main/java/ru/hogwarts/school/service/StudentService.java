package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import static java.util.stream.Collectors.toList;
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
    public List<String> findAllNamedStartingWithA() {
        logger.info("Was invoked method findAllNamedStartingWithA");
        return studentRepository.findAll().stream()
                .parallel()
                .map( student -> student.getName().toUpperCase())
                .filter(student -> student.startsWith("A"))
                .sorted()
                .toList();
    }
    public Double avgAgeAllStudents() {
        logger.info("Was invoked method avgAgeAllStudents");
        return studentRepository.findAll().stream()
                .parallel()
                .mapToInt(student -> student.getAge())
                .average()
                .stream()  // Преобразуем OptionalDouble в DoubleStream
                .map(avg -> Math.round(avg * 100) / 100.0)  // Округляем до 2 знаков после запятой
                .findFirst()
                .orElse(0);
    }
    private void doOperation( int i){
        logger.info("Was invoked method doOperation {}", i);
        List<Student> students = studentRepository.findAll();
        System.out.println(students.get(i).getName());
    }
    private synchronized void doOperationSynchronized( int i){
        logger.info("Was invoked method doOperationSynchronized {}", i);
        List<Student> students = studentRepository.findAll();
        System.out.println(students.get(i).getName());
    }
    public String printParallelThreads() {
        logger.info("Was invoked method printParallelThreads");
        doOperation(0);
        doOperation(1);
        new Thread(() -> {
            doOperation(2);
            doOperation(3);
        }).start();
        new Thread(() -> {
            doOperation(4);
            doOperation(5);
        }).start();
        return "Parallel threads completed";
    }
    public String printSynchronizedThreads() {
        logger.info("Был вызван метод printSynchronizedThreads");
        doOperationSynchronized(0);
        doOperationSynchronized(1);
        new Thread(() -> {
            doOperationSynchronized(2);
            doOperationSynchronized(3);
        }).start();
        new Thread(() -> {
            doOperationSynchronized(4);
            doOperationSynchronized(5);
        }).start();
        return "Синхронизация прошла успешно";
    }
}