package com.workouttracker.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "plan_exercises")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private WorkoutPlan plan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "target_sets")
    private Integer targetSets;

    @Column(name = "target_reps")
    private Integer targetReps;

    @Column(name = "target_weight", precision = 6, scale = 2)
    private BigDecimal targetWeight;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(columnDefinition = "text")
    private String notes;
}
