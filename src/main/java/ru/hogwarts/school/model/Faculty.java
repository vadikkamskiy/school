package ru.hogwarts.school.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Faculty {
    @Id
    @GeneratedValue
    // просто интересно... как проверяется как проект компилируется??
    private  long id;

    private String name;
    private String color;
    @JsonIgnore
    @OneToMany(mappedBy = "faculty")
    @JsonManagedReference
    private List<Student> students = new ArrayList<>();
}
