package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections; // Добавьте импорт
import java.util.Objects; // Добавьте импорт

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

    // Эндпоинт для получения всех или фильтрации по имени/цвету
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

    // Этот метод оставим для специфической фильтрации по цвету, если надо вернуть один факультет
    // Или вы можете удалить его, если getAllOrFilteredFaculty() с параметром color достаточен
    @GetMapping("/filtered-by-color/{color}")
    public ResponseEntity<Faculty> getFacultyByColor(@PathVariable String color) {
        // Этот метод, как я понял из вашего тестового кода, предполагал возврат одного факультета.
        // Если сервис findFacultyByColor возвращает коллекцию, этот метод нужно будет пересмотреть.
        // Исхожу из того, что ваш сервис findFacultyByColor может возвращать первый найденный.
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
        // Возвращаем 200 OK с созданным факультетом
        return ResponseEntity.ok(createdFaculty);
    }

    @PutMapping("{id}") // Лучше использовать ID из пути дляPUT
    public ResponseEntity<Faculty> editFaculty(@PathVariable long id, @RequestBody Faculty faculty) {
        // Перед обновлением, убедимся, что факультет с таким ID существует
        Faculty existingFaculty = facultyService.findFaculty(id);
        if (existingFaculty == null) {
            return ResponseEntity.notFound().build(); // Если нет, возвращаем 404
        }
        // Устанавливаем ID, который хотим обновить
        faculty.setId(id);
        Faculty updatedFaculty = facultyService.editFaculty(faculty);
        // Если сервис вернул null, значит что-то пошло не так (хотя мы уже проверили существование)
        if (updatedFaculty == null) {
            // В идеале, сюда не должны попасть, если findFaculty работает корректно
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

    // Эндпоинт для поиска по студенту. Если он нужен.
    // Этот метод не был покрыт тестами, но есть в контроллере.
    @GetMapping("/students/{studentName}")
    public ResponseEntity<Faculty> getFacultiesByStudentsName(@PathVariable String studentName) {
        Faculty faculty = facultyService.getFacultiesByStudentsName(studentName);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(faculty);
        }
    }
}