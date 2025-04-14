package ru.hogwarts.school.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Student>> getStudentInfo(@PathVariable Long id) {
        Optional<Student> student = studentService.findStudent(id);
        if (student.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping("/edit")
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findByAge")
    public List<Student> findByAge(@RequestParam int age) {
        return studentService.findByAge(age);
    }

    @GetMapping("/getAll")
    public List<Student> allStudents(){
        return studentService.getAll();
    }
    @GetMapping("/getByAge")
    public List<Student> findByAgeBetween(
        @RequestParam(defaultValue = "0") int byAge ,
        @RequestParam Integer toUpAge) {
        return studentService.findByAgeBetween(byAge, toUpAge);
    }
    @GetMapping("/{studentId}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long studentId) {
        Optional<Student> studentOptional = studentService.findStudent(studentId);
        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Student student = studentOptional.get();
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(faculty);
    }
}
