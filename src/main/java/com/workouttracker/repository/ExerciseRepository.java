package com.workouttracker.repository;

import com.workouttracker.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {

    List<Exercise> findByMuscleGroupIgnoreCase(String muscleGroup);

    Optional<Exercise> findByNameIgnoreCase(String name);
}
