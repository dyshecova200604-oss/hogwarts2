package ru.hogwarts.school.controller;

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
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController; // Этот autowired все еще полезен для проверки contextLoads

    @Autowired
    private TestRestTemplate restTemplate;

    private String url;
    private Faculty testFacultyForCreation; // Изменил имя для ясности

    @BeforeEach
    void setUp() {
        url = "http://localhost:" + port + "/faculty";

        testFacultyForCreation = new Faculty();
        testFacultyForCreation.setName("Test Faculty");
        testFacultyForCreation.setColor("Test Color");
    }

    @Test
    void contextLoads() { // Этот тест хорош, но мы его не используем дальше
        assertThat(facultyController).isNotNull();
    }

    // Тест объединен для лучшей инкапсуляции: создание, получение, удаление
    @Test
    void createGetAndDeleteFaculty() {
        // 1. CREATE Faculty
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(url, testFacultyForCreation, Faculty.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty createdFaculty = createResponse.getBody();
        assertThat(createdFaculty).isNotNull();
        assertThat(createdFaculty.getId()).isNotNull(); // ID должен присвоиться
        assertThat(createdFaculty.getName()).isEqualTo(testFacultyForCreation.getName());
        assertThat(createdFaculty.getColor()).isEqualTo(testFacultyForCreation.getColor());

        // 2. GET Faculty by ID
        Faculty retrievedFaculty = Objects.requireNonNull(restTemplate.getForObject(url + "/" + createdFaculty.getId(), Faculty.class));
        assertThat(retrievedFaculty).isNotNull();
        assertThat(retrievedFaculty.getId()).isEqualTo(createdFaculty.getId());
        assertThat(retrievedFaculty.getName()).isEqualTo(createdFaculty.getName());
        assertThat(retrievedFaculty.getColor()).isEqualTo(createdFaculty.getColor());

        // 3. DELETE Faculty
        assertThat(restTemplate.exchange(url + "/" + createdFaculty.getId(), HttpMethod.DELETE, null, Void.class).getStatusCode())
                .isEqualTo(HttpStatus.OK); // Проверяем, что удаление прошло успешно

        // 4. GET Faculty after DELETE (проверка, что он действительно удален)
        ResponseEntity<Faculty> getAfterDeleteResponse = restTemplate.getForEntity(url + "/" + createdFaculty.getId(), Faculty.class);
        assertThat(getAfterDeleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllFaculty() {
        // Предполагается, что в базе уже есть какие-то факультеты.
        // Этот тест будет работать, если getAllFaculty() возвращает коллекцию.
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(url, Faculty[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        // Можно проверить, что количество > 0, или оставить как есть, если тест не такой строгий
        // assertThat(response.getBody()).hasSizeGreaterThan(0);
    }

    @Test
    void getFilteredFacultyByColor() {
        // Создаем факультеты для тестирования фильтрации
        Faculty facultyRed1 = new Faculty();
        facultyRed1.setName("Слизерин Red 1");
        facultyRed1.setColor("Красный");

        Faculty facultyRed2 = new Faculty();
        facultyRed2.setName("Пуффендуй Red 2");
        facultyRed2.setColor("Красный");

        Faculty facultyGreen1 = new Faculty();
        facultyGreen1.setName("Когтевран Green 1");
        facultyGreen1.setColor("Зеленый");

        // Отправляем POST запросы
        Faculty createdRed1 = restTemplate.postForObject(url, facultyRed1, Faculty.class);
        Faculty createdRed2 = restTemplate.postForObject(url, facultyRed2, Faculty.class);
        Faculty createdGreen1 = restTemplate.postForObject(url, facultyGreen1, Faculty.class);

        // Теперь используем эндпоинт getAllOrFilteredFaculty с параметром color
        // Здесь мы ожидаем получить КОЛЛЕКЦИЮ факультетов
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(url + "?color=Красный", Faculty[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Faculty> redFaculties = Arrays.asList(response.getBody());
        assertThat(redFaculties)
                .hasSize(2) // Ожидаем 2 красных факультета
                .allSatisfy(f -> assertThat(f.getColor()).isEqualTo("Красный")); // Все должны быть красными

        // Очистка после теста
        restTemplate.delete(url + "/" + Objects.requireNonNull(createdRed1).getId());
        restTemplate.delete(url + "/" + Objects.requireNonNull(createdRed2).getId());
        restTemplate.delete(url + "/" + Objects.requireNonNull(createdGreen1).getId());
    }

    // Тест для фильтрации по имени
    @Test
    void getFilteredFacultyByName() {
        Faculty facultyGryffindor = new Faculty();
        facultyGryffindor.setName("Гриффиндор");
        facultyGryffindor.setColor("Красный и Золотой");

        Faculty facultySlytherin = new Faculty();
        facultySlytherin.setName("Слизерин");
        facultySlytherin.setColor("Зеленый и Серебряный");

        Faculty createdGryffindor = restTemplate.postForObject(url, facultyGryffindor, Faculty.class);
        Faculty createdSlytherin = restTemplate.postForObject(url, facultySlytherin, Faculty.class);

        // Фильтрация по имени "Гриффиндор"
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(url + "?name=Гриффиндор", Faculty[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Faculty> gryffindorList = Arrays.asList(response.getBody());
        assertThat(gryffindorList)
                .hasSize(1)
                .allSatisfy(f -> assertThat(f.getName()).isEqualTo("Гриффиндор"));

        // Очистка
        restTemplate.delete(url + "/" + Objects.requireNonNull(createdGryffindor).getId());
        restTemplate.delete(url + "/" + Objects.requireNonNull(createdSlytherin).getId());
    }

    @Test
    void createFaculty() {
        ResponseEntity<Faculty> response = restTemplate.postForEntity(url, testFacultyForCreation, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty createdFaculty = response.getBody();
        assertThat(createdFaculty).isNotNull();
        assertThat(createdFaculty.getName()).isEqualTo(testFacultyForCreation.getName());
        assertThat(createdFaculty.getColor()).isEqualTo(testFacultyForCreation.getColor());

        // Обязательно удалим созданный факультет
        restTemplate.delete(url + "/" + Objects.requireNonNull(createdFaculty).getId());
    }

    @Test
    void editFaculty() {
        // Сначала создаем факультет
        Faculty facultyToEdit = restTemplate.postForObject(url, testFacultyForCreation, Faculty.class);
        Objects.requireNonNull(facultyToEdit, "Failed to create faculty for edit test");
        Long facultyId = facultyToEdit.getId(); // Сохраняем ID

        // Подготавливаем данные для обновления
        Faculty updatedData = new Faculty();
        updatedData.setName("Updated Name");
        updatedData.setColor("New Color");

        // Отправляем PUT запрос с ID в URL
        HttpEntity<Faculty> requestEntity = new HttpEntity<>(updatedData);
        ResponseEntity<Faculty> response = restTemplate.exchange(url + "/" + facultyId, HttpMethod.PUT, requestEntity, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty updatedFaculty = response.getBody();
        assertThat(updatedFaculty).isNotNull();
        assertThat(updatedFaculty.getId()).isEqualTo(facultyId); // ID должен остаться тем же
        assertThat(updatedFaculty.getName()).isEqualTo("Updated Name");
        assertThat(updatedFaculty.getColor()).isEqualTo("New Color");

        // Удаляем измененный факультет
        restTemplate.delete(url + "/" + facultyId);
    }

    // Тест на удаление теперь проверяет, что объект не найден после удаления
    @Test
    void deleteFaculty() {
        // Создаем факультет для удаления
        Faculty facultyToDelete = restTemplate.postForObject(url, testFacultyForCreation, Faculty.class);
        Objects.requireNonNull(facultyToDelete, "Failed to create faculty for delete test");
        Long facultyId = facultyToDelete.getId();

        // Удаляем факультет
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(url + "/" + facultyId, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK); // Убедились, что удаление прошло

        // Проверяем, что факультет не может быть получен после удаления (ожидаем 404 Not Found)
        ResponseEntity<Faculty> getResponseAfterDelete = restTemplate.getForEntity(url + "/" + facultyId, Faculty.class);
        assertThat(getResponseAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}