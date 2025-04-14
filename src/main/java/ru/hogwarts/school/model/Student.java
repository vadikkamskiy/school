package ru.hogwarts.school.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {
    @Id
    @GeneratedValue
    private long id;

    private String name;
    private int age;

    @ManyToOne
    @JoinColumn(name = "faculty")
    @JsonBackReference 
    private Faculty faculty;
    
}
