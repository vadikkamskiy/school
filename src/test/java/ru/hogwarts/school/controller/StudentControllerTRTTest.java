package ru.hogwarts.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentControllerTRTTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Long studentId;

    private String getUrl(String uri) {
        return "http://localhost:" + port + "/student" + uri;
    }

    @Test
    @Order(1)
    void createStudent() {
        Student student = new Student();
        student.setName("Test Student");
        student.setAge(20);
        Student created = restTemplate.postForObject(getUrl(""), student, Student.class);
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        studentId = created.getId();
    }

    @Test
    @Order(2)
    void getStudentById() {
        ResponseEntity<Student> response = restTemplate.getForEntity(getUrl("/" + studentId), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test Student");
    }

    @Test
    @Order(3)
    void uploadAvatar() {
        ClassPathResource resource = new ClassPathResource("test-avatar.png"); 
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("avatar", resource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(getUrl("/" + studentId + "/avatar"), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("successfully");
    }

    @Test
    @Order(4)
    void deleteStudent() {
        restTemplate.delete(getUrl("/" + studentId));
        ResponseEntity<Student> response = restTemplate.getForEntity(getUrl("/" + studentId), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

