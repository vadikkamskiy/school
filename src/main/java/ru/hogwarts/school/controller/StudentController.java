package ru.hogwarts.school.controller;

import java.io.IOException;
import java.util.Collection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Student>> findAllStudents() {
        return ResponseEntity.ok(studentService.findAllStudents());
    }

    @GetMapping("/byAge")
    public ResponseEntity<Collection<Student>> findStudentsByAge(@RequestParam Integer age) {
        if (age > 0) {
            return ResponseEntity.ok(studentService.findByAge(age));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        return studentService.findStudent(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) {
        try {
           if (avatar.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }
            if (avatar.getSize() > 1024 * 300) {
                return ResponseEntity.badRequest().body("File is too big");
            }
            studentService.uploadAvatar(id, avatar);
            return ResponseEntity.ok("Avatar uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading avatar: " + e.getMessage());
        }
    }
    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
     try {
        Avatar avatar = studentService.findAvatar(id);
        if (avatar == null || avatar.getData() == null || avatar.getData().length == 0) {
            return ResponseEntity.notFound().build();
        }
        String mediaType = avatar.getMediaType();
        if(!MediaType.parseMediaTypes(mediaType).stream().findFirst().isPresent()){
            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, mediaType)
            .header(HttpHeaders.CONTENT_DISPOSITION,"inline")
            .body(avatar.getData());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/count")
    public long countStudents() {
        return studentService.countAllStudents();
    }
    @GetMapping("/average-age")
    public Double getAverageAge() {
        return studentService.getAverageAge();
    }
    @GetMapping("/last-five")
    public Collection<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }
}