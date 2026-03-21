package ru.hogwarts.school.controller;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class StudentControllerTestRest {
    @ru.hogwarts.school.LocalServerPort
    private final int port;

    @Autowired
    private TestRestTemplate restTemplate;

    public StudentControllerTestRest(int port) {
        this.port = port;
    }

    @Test
    public void testGetFaculty() throws Exception {
        Student student = new Student();
        student.setStudentId(1L);
        student.setName("Kate");
        student.setAge(4);

        Student actual = this.restTemplate.getForObject("http://localhost:" + port + "/faculty/{1L}", Student.class, student.getId());

        Assertions.assertThat(actual.getId()).isEqualTo(1L);
        Assertions.assertThat(actual.getName()).isEqualTo("Kate");
        Assertions.assertThat(actual.getAge()).isEqualTo(4);
    }

    @Test
    public void testPostStudent() throws Exception {
        Student student = new Student();
        student.setName("Kate");
        student.setAge(4);
        Student actual = this.restTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);

        Assertions.assertThat(actual.getId()).isNotNull();
        Assertions.assertThat(actual.getName()).isEqualTo("Kate");
        Assertions.assertThat(actual.getAge()).isEqualTo(4);
    }

    @Test
    public void testPutStudent() throws Exception {
        Student student = new Student();
        student.setName("Mary");
        student.setAge(30);
        Student actual = this.restTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);
        actual.setName("Robin");
        actual.setAge(8);
        ResponseEntity <Student> updated = this.restTemplate.exchange
                ("http://localhost:" + port + "/student/" + actual.getId(), HttpMethod.PUT, new HttpEntity<>(actual), Student.class);

        Assertions.assertThat(updated.getBody().getId()).isNotNull();
        Assertions.assertThat(updated.getBody().getName()).isEqualTo("Robin");
        Assertions.assertThat(updated.getBody().getAge()).isEqualTo(8);

    }

    @Test
    public void testDeleteStudent() throws Exception {
        Student student = new Student();
        student.setName("Mary");
        student.setAge(30);
        Faculty actual = this.restTemplate.postForObject("http://localhost:" + port + "/faculty", student, Faculty.class);
        ResponseEntity <String> response = restTemplate.exchange
                ("http://localhost:" + port + "/student/" + actual.getStudents(), HttpMethod.DELETE, null, String.class);

        Student s = restTemplate.getForObject("http://localhost:" + port + "/student/" +
                actual.getStudents(), Student.class);
        Assertions.assertThat(s).isNull();

    }

    @Test
    public void testGetStudentsByAge() throws Exception {

    }

    public void TestGetFacultyByStudentId() throws Exception {

    }
}