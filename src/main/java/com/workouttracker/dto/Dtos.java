package com.workouttracker.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class Dtos {

    private Dtos() {}

    public record RegisterRequest(
            @Email @NotBlank String email,
            @NotBlank @Size(min = 3, max = 50) String username,
            @NotBlank @Size(min = 8) String password) {}

    public record LoginRequest(
            @Email @NotBlank String email,
            @NotBlank String password) {}

    public record AuthResponse(
            UUID id,
            String email,
            String username,
            String token) {}

    public record CreatePlanRequest(
            @NotBlank String name,
            String description) {}

    public record AddPlanExerciseRequest(
            @NotNull UUID exerciseId,
            Integer targetSets,
            Integer targetReps,
            BigDecimal targetWeight,
            Integer orderIndex,
            String notes) {}

    public record PlanExerciseResponse(
            UUID id, UUID exerciseId, String exerciseName,
            Integer targetSets, Integer targetReps,
            BigDecimal targetWeight, Integer orderIndex, String notes) {}

    public record PlanResponse(
            UUID id, String name, String description,
            Instant createdAt, List<PlanExerciseResponse> exercises) {}

    public record StartSessionRequest(UUID planId, String name) {}

    public record LogSetRequest(
            @NotNull UUID exerciseId,
            @NotNull @Min(1) Integer setNumber,
            @Min(1) Integer reps,
            @DecimalMin("0.0") BigDecimal weight,
            @Min(1) Integer durationSeconds,
            @DecimalMin("0.0") BigDecimal distanceMeters,
            Boolean completed) {}

    public record SessionSummary(
            UUID id, String name,
            Instant startedAt, Instant completedAt,
            long setCount) {}

    public record SetResponse(
            UUID id, UUID exerciseId, String exerciseName,
            Integer setNumber, Integer reps, BigDecimal weight,
            Integer durationSeconds, BigDecimal distanceMeters,
            Boolean completed) {}

    public record SessionDetail(
            UUID id, String name,
            Instant startedAt, Instant completedAt,
            String notes, List<SetResponse> sets) {}

    public record SummaryResponse(
            long totalSessions, long totalSets, long totalReps) {}

    public record ProgressPoint(
            String date, BigDecimal weight, Integer reps) {}

    public record PersonalRecord(
            String exercise, BigDecimal weight,
            Integer reps, Instant date) {}
}