package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;
    private final StudentService studentService;

    public FacultyController(FacultyService facultyService, StudentService studentService) {
        this.facultyService = facultyService;
        this.studentService = null;
    }

    // @GetMapping("{id}")
    // public ResponseEntity<Optional<Faculty>> getFacultyInfo(@PathVariable Long id) {
    //     Optional<Faculty> faculty = facultyService.findFaculty(id);
    //     if (faculty.isEmpty() || faculty.get().getName().isBlank()) {
    //         return ResponseEntity.notFound().build();
    //     }
    //     return ResponseEntity.ok(faculty);
    // }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findByColor")
    public List<Faculty> findByColor(@RequestParam String color) {
        return facultyService.findByColorIgnoreCase(color);
    }
    @GetMapping("/getAll")
    public List<Faculty> allFaculties(){
        return facultyService.getAll();
    }
    @GetMapping("/{facultyId}/students")
    public ResponseEntity<List<Student>> getFacultyStudents(@PathVariable Long facultyId) {
    List<Student> students = studentService.getStudentsByFacultyId(facultyId);
    return students.isEmpty() 
        ? ResponseEntity.noContent().build() 
        : ResponseEntity.ok(students);
}
}