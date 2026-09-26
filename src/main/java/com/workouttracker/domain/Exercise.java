package com.workouttracker.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true,length = 100)
    private String name;

    @Column(name = "muscle_group", length = 50)
    private String muscleGroup;

    @Column(columnDefinition = "text")
    private String description;

}
