package com.workouttracker.repository;

import com.workouttracker.domain.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, UUID> {
    List<WorkoutPlan> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<WorkoutPlan> findByIdAndUserId(UUID id, UUID userId);
}
