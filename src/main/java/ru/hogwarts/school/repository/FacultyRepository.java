package ru.hogwarts.school.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;
import java.util.Collection;
import java.util.List;
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByColor(String color);
    List<Faculty> findFacultyByNameIgnoreCase (String name);
    Faculty getFacultiesByStudentsName (String name);
    List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);
}