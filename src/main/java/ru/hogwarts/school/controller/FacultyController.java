package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;
import java.util.Collection;


@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable long id) { // Изменил имя метода для ясности
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(faculty);
        }
    }


    @GetMapping
    public ResponseEntity<Collection<Faculty>> getAllOrFilteredFaculty(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color) {

        if (name != null && !name.trim().isEmpty()) {
            // Фильтруем по имени, если имя предоставлено и не пустое
            return ResponseEntity.ok(facultyService.findFacultyByName(name));
        } else if (color != null && !color.trim().isEmpty()) {
            // Фильтруем по цвету, если цвет предоставлен и не пустой
            return ResponseEntity.ok(facultyService.findFacultyByColor(color));
        } else {
            // Возвращаем всех, если никаких фильтров не указано
            return ResponseEntity.ok(facultyService.getAllFaculty());
        }
    }


    @GetMapping("/filtered-by-color/{color}")
    public ResponseEntity<Faculty> getFacultyByColor(@PathVariable String color) {

        Collection<Faculty> faculties = facultyService.findFacultyByColor(color);
        if (faculties == null || faculties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Возвращаем первый найденный факультет
        return ResponseEntity.ok(faculties.iterator().next());
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.ok(createdFaculty);
    }

    @PutMapping("{id}")
    public ResponseEntity<Faculty> editFaculty(@PathVariable long id, @RequestBody Faculty faculty) {
        // Перед обновлением, убедимся, что факультет с таким ID существует
        Faculty existingFaculty = facultyService.findFaculty(id);
        if (existingFaculty == null) {
            return ResponseEntity.notFound().build(); // Если нет, возвращаем 404
        }
        // Устанавливаем ID, который хотим обновить
        faculty.setId(id);
        Faculty updatedFaculty = facultyService.editFaculty(faculty);

        if (updatedFaculty == null) {

            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(updatedFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteFaculty(@PathVariable long id) {
        Faculty facultyToDelete = facultyService.findFaculty(id);
        if (facultyToDelete == null) {
            return ResponseEntity.notFound().build(); // Если не найден, возвращаем 404
        }
        facultyService.deleteFaculty(id);
        // Успешное удаление
        return ResponseEntity.ok().build(); // Или ResponseEntity.noContent().build();
    }


    @GetMapping("/students/{studentName}")
    public ResponseEntity<Faculty> getFacultiesByStudentsName(@PathVariable String studentName) {
        Faculty faculty = facultyService.getFacultiesByStudentsName(studentName);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(faculty);
        }
    }
    @GetMapping("/longestName")
    public ResponseEntity<String> getFacultyLongestName() {
        Faculty faculty = facultyService.getFacultyLongestName();
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(faculty.getName());
        }
    }
    @GetMapping("/example4")
    public Long example4(){
        return facultyService.example4();
    }

}