package com.workouttracker.controller;

import com.workouttracker.domain.Exercise;
import com.workouttracker.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService service;

    @GetMapping
    public List<Exercise> list(@RequestParam(required = false) String muscleGroup){
        return service.list(muscleGroup);
    }

    @GetMapping("/{id}")
    public Exercise get(@PathVariable UUID id){
        return service.get(id);
    }
}
