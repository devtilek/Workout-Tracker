package com.workouttracker.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workout_plans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false,length = 100)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL,orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    @Builder.Default
    private List<PlanExercise> exercises = new ArrayList<>();

    @PrePersist
    void onCreate(){
        if (createdAt == null) createdAt = Instant.now();
    }
}
