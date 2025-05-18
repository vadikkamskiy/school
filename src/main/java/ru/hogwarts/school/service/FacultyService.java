package ru.hogwarts.school.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import java.util.*;

@Slf4j
@Service
public class FacultyService {
    @Autowired
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        log.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Optional<Faculty> findFaculty(long id) {
        log.info("Was invoked method for find faculty");
        return facultyRepository.findById(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        log.info("Was invoked method for edit faculty");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        log.info("Was invoked method for delete faculty " + facultyRepository.findById(id));
        if (facultyRepository.findById(id).isEmpty()) {
            log.error("Faculty with id " + id + " not found");
        }
        facultyRepository.deleteById(id);
    }

    public List<Faculty> findByColorIgnoreCase(String color){
        log.info("Was invoked method for find faculty by color " + color);
        return facultyRepository.findByColorIgnoreCase(color);
    }
    public List<Faculty> getAll(){
        log.info("Was invoked method for get all faculty");
        return facultyRepository.findAll();
    }
}
