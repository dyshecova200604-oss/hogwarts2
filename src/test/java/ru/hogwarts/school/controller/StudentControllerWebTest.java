package ru.hogwarts.school.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentRepository studentRepository;

    @MockitoSpyBean
    private StudentService studentService;

    @MockitoBean
    private FacultyService facultyService;

    private Student testStudent;

    private final JSONObject facultyObject = new JSONObject();
    private final JSONObject studentObject = new JSONObject();

    @BeforeEach
    void init() throws JSONException {

        long id = 1000;
        String name = "Harry Potter";
        int age = 25;

        String nameFaculty = "Gryffindor";
        String color = "red";

        testStudent = new Student();
        testStudent.setName(name);
        testStudent.setAge(age);
        testStudent.setId(id);

        studentObject.put("id", testStudent.getId());
        studentObject.put("name", testStudent.getName());
        studentObject.put("age", testStudent.getAge());

        Faculty testFaculty = new Faculty();

        testFaculty.setName(nameFaculty);
        testFaculty.setColor(color);

        facultyObject.put("id", testFaculty.getId());
        facultyObject.put("name", testFaculty.getName());
        facultyObject.put("color", testFaculty.getColor());

        testStudent.setFaculty(testFaculty);
    }

    @Test
    public void testCreateStudent() throws Exception {

        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testStudent.getId()))
                .andExpect(jsonPath("$.name").value(testStudent.getName()))
                .andExpect(jsonPath("$.age").value(testStudent.getAge()));
    }

    @Test
    public void testGetStudentById() throws Exception {

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(testStudent));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/" + testStudent.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testStudent.getId()))
                .andExpect(jsonPath("$.name").value(testStudent.getName()))
                .andExpect(jsonPath("$.age").value(testStudent.getAge()))
                .andExpect(status().isOk());
    }

    @Test
    public void editStudent() throws Exception{

        Student changedStudent = new Student();
        changedStudent.setId(testStudent.getId());
        changedStudent.setName("Hermione");
        changedStudent.setAge(25);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(changedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(changedStudent.getId()))
                .andExpect(jsonPath("$.name").value(changedStudent.getName()))
                .andExpect(jsonPath("$.age").value(changedStudent.getAge()));
    }

    @Test
    public void deleteStudent() throws Exception{

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(testStudent));
        doNothing().when(studentRepository).deleteById(testStudent.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/" + testStudent.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(studentRepository, times(1)).deleteById(testStudent.getId());
    }
    @Test
    public void deleteStudentNotFoundName() throws Exception{

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(studentService).deleteStudent(testStudent.getId());

        mockMvc.perform(delete("/students/" + testStudent.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getStudentsByFacultyName() throws Exception{

        String foundFacultyName = "Gryffindor";

        List<Student> mockStudents = Collections.singletonList(testStudent);

        when(studentService.getStudentsByFacultyName(foundFacultyName))
                .thenReturn(mockStudents);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/faculty/{name}", foundFacultyName)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString())
                .contains("Harry Potter");
    }

}