package com.workouttracker.controller;

import com.workouttracker.dto.Dtos;
import com.workouttracker.service.WorkoutPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class WorkoutPlanController {
    private final WorkoutPlanService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dtos.PlanResponse create(@Valid @RequestBody Dtos.CreatePlanRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<Dtos.PlanResponse> list() {
        return service.list();
    }

    @GetMapping("/{planId}")
    public Dtos.PlanResponse get(@PathVariable UUID planId) {
        return service.get(planId);
    }

    @PostMapping("/{planId}/exercises")
    public Dtos.PlanResponse addExercise(
            @PathVariable UUID planId,
            @Valid @RequestBody Dtos.AddPlanExerciseRequest request) {
        return service.addExercise(planId, request);
    }

    @DeleteMapping("/{planId}/exercises/{planExerciseId}")
    public Dtos.PlanResponse removeExercise(
            @PathVariable UUID planId,
            @PathVariable UUID planExerciseId) {
        return service.removeExercise(planId, planExerciseId);
    }

    @DeleteMapping("/{planId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID planId) {
        service.delete(planId);
    }
}
