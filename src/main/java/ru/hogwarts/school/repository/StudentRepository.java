package ru.hogwarts.school.repository;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long>{
    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int before, int toUpAge);
    List<Student> findByFacultyId(Long facultyId);

    @Query("SELECT COUNT(s) FROM Student s")
    long countAllStudents();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double getAverageAge();

    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> findTop5ByOrderByIdDesc(Pageable pageable);
    Collection<Student> findTop5ByOrderByIdDesc(PageRequest of);

    @Query("SELECT s.name FROM Student s")
    List<String> findAllNames();

}
