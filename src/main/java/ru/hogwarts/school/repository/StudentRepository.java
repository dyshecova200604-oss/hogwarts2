package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long>{
    List<Student> findByAge (int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> getStudentsByFacultyName(String name);
    @Query(value = "select count(*) from student", nativeQuery = true)
    Long countAllStudents();

    @Query(value = "select avg(age) from student", nativeQuery = true)
    Integer averageAgeOfStudents();

    @Query(value = "select * from student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getFiveLastStudents();

}