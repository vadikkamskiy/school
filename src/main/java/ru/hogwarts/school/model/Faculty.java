package ru.hogwarts.school.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@Entity
public class Faculty {
    //@Id
    //@GeneratedValue
    private  long id;

    private String name;
    private String color;
}
