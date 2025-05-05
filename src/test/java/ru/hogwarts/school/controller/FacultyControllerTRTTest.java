package ru.hogwarts.school.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FacultyControllerTRTTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Long facultyId;

    private String getUrl(String uri) {
        return "http://localhost:" + port + "/faculty" + uri;
    }

    @Test
    @Order(1)
    void createFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("white");
        Faculty created = restTemplate.postForObject(getUrl(""), faculty, Faculty.class);
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        facultyId = created.getId();
    }

    @Test
    @Order(2)
    void getFaculty() {
        Faculty created = restTemplate.getForObject(getUrl("/" + facultyId), Faculty.class);
        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(facultyId);
        assertThat(created.getName()).isEqualTo("Test Faculty");
        assertThat(created.getColor()).isEqualTo("white");
    }

    @Test
    @Order(3)
    void editFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName("Updated Faculty");
        faculty.setColor("Purple");

        HttpEntity<Faculty> requestEntity = new HttpEntity<>(faculty);
        ResponseEntity<Faculty> response = restTemplate.exchange(
                getUrl(""), HttpMethod.PUT, requestEntity, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Faculty updated = restTemplate.getForObject(getUrl("/" + facultyId), Faculty.class);
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("Updated Faculty");
        assertThat(updated.getColor()).isEqualTo("Purple");
    }

    @Test
    @Order(4)
    void getFacultyByColor() {
        Faculty[] faculties = restTemplate.getForObject(getUrl("/findByColor?color=Purple"), Faculty[].class);
        assertThat(faculties).isNotNull();
        assertThat(faculties.length).isGreaterThan(0);
        assertThat(faculties[0].getName()).isEqualTo("Updated Faculty");
        assertThat(faculties[0].getColor()).isEqualTo("Purple");
    }

    @Test
    @Order(5)
    void deleteFaculty() {
        restTemplate.delete(getUrl("/" + facultyId));
        ResponseEntity<Faculty> response = restTemplate.getForEntity(getUrl("/" + facultyId), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
