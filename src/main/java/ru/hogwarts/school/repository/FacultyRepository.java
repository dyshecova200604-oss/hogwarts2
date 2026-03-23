package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long>{
    Faculty findByColor(String color);

    List<Faculty> findFacultyByNameIgnoreCase (String name);

    List<Faculty> findFacultyByColorIgnoreCase (String color);

    Faculty getFacultiesByStudentsName (String name);
}