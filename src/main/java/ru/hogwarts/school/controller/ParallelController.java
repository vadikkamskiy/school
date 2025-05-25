package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/parallel")
public class ParallelController {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

   @GetMapping("/nameStartWithA")
   public List<String> getNamesStartingWithA() {
        return studentRepository.findAll()
                .parallelStream()
                .map(Student::getName)
                .filter(name -> name.startsWith("А"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    @GetMapping("/student/average-age")
    public double getAverageAge() {
        return studentRepository.findAll()
                .parallelStream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    @GetMapping("/faculty/longest-name")
    public Optional<String> getLongestFacultyName() {
        return facultyRepository.findAll()
                .parallelStream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length));
    }

    @GetMapping("/fast-sum")
    public int fastSum() {
        return IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .sum();
    }
}
