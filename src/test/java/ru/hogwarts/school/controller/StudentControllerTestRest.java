package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentController studentController;

    private String url;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port + "/students";

        testStudent = new Student();
        testStudent.setName("testName");
        testStudent.setAge(10);
        testStudent.setId(100);
    }
    @Test
    void contextLoads() throws Exception {
        assertThat(studentController).isNotNull();
    }

    @Test
    void getStudent() {
        Student postStudent = restTemplate.postForObject(url, testStudent.getId(), Student.class);
        Student getStudent = restTemplate.getForObject(url + "/" + postStudent.getId(), Student.class);

        assertThat(getStudent).isNotNull();
        assertThat(postStudent).isEqualTo(getStudent);

        restTemplate.delete(url + "/" + postStudent.getId());

        Student deletedStudent = this.restTemplate.getForObject(url + "/" + postStudent.getId(), Student.class);
        System.out.println(deletedStudent);
        assertThat(deletedStudent.getName()).isNull();

        restTemplate.delete(url + "/" + testStudent.getId());
    }

    @Test
    void getAllStudents() {
        assertThat(restTemplate.getForObject(url, Student[].class)).isNotEmpty();
        assertThat(restTemplate.getForObject(url, Student[].class)).hasSize(45);
    }

    @Test
    void getFilteredStudentsByAge() {
        Assertions
                .assertThat(this.restTemplate.getForObject(url + "?age=" + 10, Student[].class))
                .isNotNull()
                .hasSize(45);
    }

    @Test
    void createStudent() {

        ResponseEntity<Faculty> responseStudent = restTemplate.postForEntity(url, testStudent, Faculty.class);

        assertThat(responseStudent.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseStudent.getBody()).isNotNull();
        assertThat(responseStudent.getBody().getName()).isEqualTo(testStudent.getName());

        restTemplate.delete(url + "/" + testStudent.getId());
        restTemplate.delete(url + "/" + responseStudent.getBody().getId());

    }

    @Test
    void editStudent() {
        Student student10 = new Student();
        student10.setId(1040);
        student10.setName("student10");
        student10.setAge(200);

        Student student11 = restTemplate.postForObject(url, student10, Student.class);
        Student student = restTemplate.getForObject(url + "/" + student11.getId(), Student.class);
        student.setName("newName");

        assertThat(student.getName()).isEqualTo("newName");

        restTemplate.delete(url + "/" + student11.getId());
    }

    @Test
    void deleteStudent() throws Exception {
        Student student1 = new Student();
        student1.setId(1000);
        student1.setName("student1");
        student1.setAge(20);

        Student student2 = new Student();
        student2.setId(1001);
        student2.setName("student2");
        student2.setAge(21);

        ResponseEntity<Student> postStudentresponse = restTemplate.postForEntity(url, student2, Student.class);

        Student postStudent = restTemplate.postForObject(url, student1, Student.class);

        assertThat(postStudentresponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions
                .assertThat(this.restTemplate.getForObject(url + "/" + postStudent.getId(), Student.class))
                .isNotNull();

        Assertions
                .assertThat(postStudentresponse.getBody().getName()).isEqualTo(student2.getName());

        restTemplate.delete(url + "/" + postStudent.getId());
        restTemplate.delete(url + "/" + postStudentresponse.getBody().getId());

        Assertions
                .assertThat(this.restTemplate.getForObject(url + "/" + postStudent.getId(), Student.class).getName())
                .isNull();

        System.out.println(postStudent.getId() + "  " + postStudentresponse.getBody().getId());
        restTemplate.delete(url + "/" + postStudent.getId());
        restTemplate.delete(url + "/" + postStudentresponse.getBody().getId());
        System.out.println(postStudent.getId() + "  " + postStudentresponse.getBody().getId());
    }

    public void setPort(int port) {
        this.port = port;
    }
}