package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url;
    private Faculty testFaculty;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port + "/faculty";

        testFaculty = new Faculty();
        testFaculty.setId(100);
        testFaculty.setName("Test Faculty");
        testFaculty.setColor("Test Color");
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test
    void getFaculty() throws Exception {
        Faculty testPostFaculty = restTemplate.postForObject(url, testFaculty, Faculty.class);

        Faculty testGetFaculty = restTemplate.getForObject(url + "/" + testPostFaculty.getId(), Faculty.class);

        assertThat(testPostFaculty).isEqualTo(testGetFaculty);
    }

    @Test
    void getAllFaculty() throws Exception {

        assertThat(this.restTemplate.getForObject(url, Faculty[].class))
                .isNotNull()
                .hasSize(7);
    }

    @Test
    void getFilteredFacultyByColor() throws Exception {
        Faculty faculty6 = new Faculty();
        faculty6.setId(6);
        faculty6.setName("Слизерин getFilteredFacultyByColor");
        faculty6.setColor("Красный");

        Faculty faculty7 = new Faculty();
        faculty7.setId(7);
        faculty7.setName("Пуффендуй getFilteredFacultyByColor");
        faculty7.setColor("Красный");

        Faculty faculty8 = new Faculty();
        faculty8.setId(8);
        faculty8.setName("Когтевран getFilteredFacultyByColor");
        faculty8.setColor("Зеленый");

        restTemplate.postForObject(url, faculty6, Faculty.class);
        restTemplate.postForObject(url, faculty7, Faculty.class);
        restTemplate.postForObject(url, faculty8, Faculty.class);

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(url + "/color/Красный", Faculty[].class);

        List<Faculty> redFaculties = Arrays.asList(response.getBody());
        assertThat(redFaculties)
                .hasSize(2)
                .allMatch(f -> f.getColor().equals("Красный"));
    }

    @Test
    void createFaculty() throws Exception {
        ResponseEntity<Faculty> response = restTemplate.postForEntity(url, testFaculty, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(testFaculty.getName());

        restTemplate.delete(url + "/" + testFaculty.getId());
    }

    @Test
    void editFaculty() throws Exception {
        Faculty testUpdatedFaculty = restTemplate.postForObject(url, testFaculty, Faculty.class);
        testUpdatedFaculty.setName("Updated Name");

        ResponseEntity<Faculty> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(testUpdatedFaculty), Faculty.class);

        assertThat(testUpdatedFaculty.getName()).isEqualTo("Updated Name");

        restTemplate.delete(url + "/" + testUpdatedFaculty.getId());
    }

    @Test
    void deleteFaculty() throws Exception {

        Faculty faculty = restTemplate.postForObject(url, testFaculty, Faculty.class);

        restTemplate.delete(url + "/" + faculty.getId());

        Assertions
                .assertThat(this.restTemplate.getForObject(url + "/" + faculty.getId(), Faculty.class))
                .isNotNull();
    }
}