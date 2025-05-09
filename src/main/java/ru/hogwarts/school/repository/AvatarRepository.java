package ru.hogwarts.school.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Avatar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByStudentId(Long studentId);
    void deleteByStudentId(Long studentId);
    Page<Avatar> findAll(Pageable pageable);
}
