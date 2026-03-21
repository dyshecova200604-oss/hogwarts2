package ru.hogwarts.school.controller;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyRepository facultyRepository;

    @ru.hogwarts.school.controller.MockitoSpyBean
    private FacultyService facultyService;

    @MockitoSpyBean
    private StudentService studentService;

    private Faculty testFaculty;

    private final JSONObject facultyObject = new JSONObject();
    private final JSONObject studentObject = new JSONObject();

    @BeforeEach
    void init() throws JSONException {

        long id = 1000;
        String name = "Harry Potter";
        int age = 25;

        String nameFaculty = "Gryffindor";
        String color = "red";

        Student testStudent = new Student();
        testStudent.setName(name);
        testStudent.setAge(age);
        testStudent.setId(id);

        studentObject.put("id", testStudent.getId());
        studentObject.put("name", testStudent.getName());
        studentObject.put("age", testStudent.getAge());

        testFaculty = new Faculty();
        testFaculty.setName(nameFaculty);
        testFaculty.setColor(color);

        facultyObject.put("id", testFaculty.getId());
        facultyObject.put("name", testFaculty.getName());
        facultyObject.put("color", testFaculty.getColor());

        testStudent.setFaculty(testFaculty);
    }

    @Test
    public void testCreateFaculty() throws Exception {

        when(facultyRepository.save(any(Faculty.class))).thenReturn(testFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testFaculty.getId()))
                .andExpect(jsonPath("$.name").value(testFaculty.getName()))
                .andExpect(jsonPath("$.color").value(testFaculty.getColor()));
    }

    @Test
    public void testGetFacultyById() throws Exception {

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(testFaculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + testFaculty.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testFaculty.getId()))
                .andExpect(jsonPath("$.name").value(testFaculty.getName()))
                .andExpect(jsonPath("$.color").value(testFaculty.getColor()))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditFaculty() throws Exception {

        String changedColorFaculty = "Blue";
        testFaculty.setColor(changedColorFaculty);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(testFaculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(testFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + testFaculty.getId())
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.color").value(changedColorFaculty))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(testFaculty));
        doNothing().when(facultyRepository).deleteById(testFaculty.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + testFaculty.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(facultyRepository, times(1)).deleteById(testFaculty.getId());
    }
    @Test
    public void deleteFacultyNotFoundName() throws Exception{

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(facultyService).deleteFaculty(testFaculty.getId());

        mockMvc.perform(delete("/faculty/" + testFaculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getFacultyByColor() throws Exception {
        when(facultyService.getFacultyByColor(any(String.class))).thenReturn((List<Faculty>) testFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color/" + testFaculty.getColor())
                        .content(facultyObject.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.color").value(testFaculty.getColor()))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFacultyByStudentsName() throws Exception {

        when(facultyService.getStudentByFaculty());

        Student mockStudent = new Student();
        mockStudent.setName("Harry Potter");
        mockStudent.setFaculty(testFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/students/" + mockStudent.getName())
                        .content(facultyObject.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(testFaculty.getName()));
    }

}