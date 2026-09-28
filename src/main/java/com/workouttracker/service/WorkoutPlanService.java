package com.workouttracker.service;

import com.workouttracker.domain.Exercise;
import com.workouttracker.domain.PlanExercise;
import com.workouttracker.domain.User;
import com.workouttracker.domain.WorkoutPlan;
import com.workouttracker.dto.Dtos;
import com.workouttracker.repository.ExerciseRepository;
import com.workouttracker.repository.UserRepository;
import com.workouttracker.repository.WorkoutPlanRepository;
import com.workouttracker.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService {
    private final WorkoutPlanRepository planRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    @Transactional
    public Dtos.PlanResponse create(Dtos.CreatePlanRequest request) {
        User user = getCurrentUser();

        WorkoutPlan plan = WorkoutPlan.builder()
                .user(user)
                .name(request.name().trim())
                .description(request.description())
                .build();

        return toResponse(planRepository.save(plan));
    }

    @Transactional(readOnly = true)
    public List<Dtos.PlanResponse> list() {
        return planRepository.findByUserIdOrderByCreatedAtDesc(currentUser.id())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Dtos.PlanResponse get(UUID planId) {
        return toResponse(getPlan(planId));
    }

    @Transactional
    public Dtos.PlanResponse addExercise(UUID planId, Dtos.AddPlanExerciseRequest request) {
        WorkoutPlan plan = getPlan(planId);
        Exercise exercise = exerciseRepository.findById(request.exerciseId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Exercise not found"));

        boolean alreadyAdded = plan.getExercises().stream()
                .anyMatch(item -> item.getExercise().getId().equals(exercise.getId()));

        if (alreadyAdded) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Exercise already exists in this plan");
        }

        PlanExercise planExercise = PlanExercise.builder()
                .plan(plan)
                .exercise(exercise)
                .targetSets(request.targetSets())
                .targetReps(request.targetReps())
                .targetWeight(request.targetWeight())
                .orderIndex(request.orderIndex())
                .notes(request.notes())
                .build();

        plan.getExercises().add(planExercise);
        return toResponse(planRepository.save(plan));
    }

    @Transactional
    public void delete(UUID planId) {
        planRepository.delete(getPlan(planId));
    }

    @Transactional
    public Dtos.PlanResponse removeExercise(UUID planId, UUID planExerciseId) {
        WorkoutPlan plan = getPlan(planId);

        boolean removed = plan.getExercises().removeIf(
                item -> item.getId().equals(planExerciseId));

        if (!removed) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Plan exercise not found");
        }

        return toResponse(planRepository.save(plan));
    }

    private WorkoutPlan getPlan(UUID planId) {
        return planRepository.findByIdAndUserId(planId, currentUser.id())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Workout plan not found"));
    }

    private User getCurrentUser() {
        return userRepository.findById(currentUser.id())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Dtos.PlanResponse toResponse(WorkoutPlan plan) {
        List<Dtos.PlanExerciseResponse> exercises = plan.getExercises().stream()
                .map(item -> new Dtos.PlanExerciseResponse(
                        item.getId(),
                        item.getExercise().getId(),
                        item.getExercise().getName(),
                        item.getTargetSets(),
                        item.getTargetReps(),
                        item.getTargetWeight(),
                        item.getOrderIndex(),
                        item.getNotes()))
                .toList();

        return new Dtos.PlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getDescription(),
                plan.getCreatedAt(),
                exercises);
    }
}
