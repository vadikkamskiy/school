package ru.hogwarts.school.service;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

@Slf4j
@Service
public class StudentService {

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student addStudent(Student student) {
        log.info("Was invoked method for create student with id " + student.getId());
        return studentRepository.save(student);
    }

    public Optional<Student> findStudent(long id) {
        log.info("Was invoked method for find student with id " + id);
        if (studentRepository.findById(id).isEmpty()) {
            log.error("Student with id " + id + " not found");
        }
        return studentRepository.findById(id);
    }

    public Collection<Student> findAllStudents() {
        log.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Student editStudent(Student student) {
        log.info("Was invoked method for edit student with name " + student.getName());
        return studentRepository.save(student);
    }
    @Transactional
    public void deleteStudent(long id) {
        log.info("Was invoked method for delete student " + studentRepository.findById(id).get().getName());
        avatarRepository.deleteByStudentId(id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(int age) {
        log.info("Was invoked method for find student by age " + age);
        if (studentRepository.findByAge(age).isEmpty()) {
            log.error("Student with age " + age + " not found");
        }
        return studentRepository.findByAge(age);
    }

    public Avatar findAvatar(long studentId) {
        log.info("Was invoked method for find avatar by student id " + studentId);
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }
    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        log.info("Was invoked method for upload avatar by student " + studentRepository.findById(studentId).get().getName());
        if (file.isEmpty()) {
            log.error("File is empty");
            throw new IllegalArgumentException("File is empty");
        }
        Student student = studentRepository.getReferenceById(studentId);
    
        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
    
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
    
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
    
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatarRepository.save(avatar);
    }

    public long countAllStudents() {
        log.info("Was invoked method for count all students");
        return studentRepository.countAllStudents();
    }
    
    public Double getAverageAge() {
        log.info("Was invoked method for get average age");
        return studentRepository.getAverageAge();
    }
    
    public Collection<Student> getLastFiveStudents() {
        log.info("Was invoked method for get last five students");
        return studentRepository.findTop5ByOrderByIdDesc(org.springframework.data.domain.PageRequest.of(0, 5));
    }

    private String getExtension(String fileName) {
        log.info("Was invoked method for get extension");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
