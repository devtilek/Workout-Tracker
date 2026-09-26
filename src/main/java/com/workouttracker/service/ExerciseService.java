package com.workouttracker.service;

import com.workouttracker.domain.Exercise;
import com.workouttracker.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository repository;

    public List<Exercise> list(String muscleGroup){
        return(muscleGroup == null || muscleGroup.isBlank())
                ? repository.findAll()
                : repository.findByMuscleGroupIgnoreCase(muscleGroup);
    }

    public Exercise get(UUID id){
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));
    }
}
