package com.workouttracker.service;

import com.workouttracker.domain.Exercise;
import com.workouttracker.domain.SessionSet;
import com.workouttracker.domain.User;
import com.workouttracker.domain.WorkoutPlan;
import com.workouttracker.domain.WorkoutSession;
import com.workouttracker.dto.Dtos;
import com.workouttracker.repository.ExerciseRepository;
import com.workouttracker.repository.SessionSetRepository;
import com.workouttracker.repository.UserRepository;
import com.workouttracker.repository.WorkoutPlanRepository;
import com.workouttracker.repository.WorkoutSessionRepository;
import com.workouttracker.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {
    private final WorkoutSessionRepository sessionRepository;
    private final SessionSetRepository setRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutPlanRepository planRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    @Transactional
    public Dtos.SessionDetail start(Dtos.StartSessionRequest request) {
        User user = getCurrentUser();
        WorkoutPlan plan = null;

        if (request.planId() != null) {
            plan = planRepository.findByIdAndUserId(request.planId(), user.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Workout plan not found"));
        }

        String name = request.name() != null && !request.name().isBlank()
                ? request.name().trim()
                : plan != null ? plan.getName() : "Workout";

        WorkoutSession session = WorkoutSession.builder()
                .user(user)
                .plan(plan)
                .name(name)
                .build();

        return toDetail(sessionRepository.save(session));
    }

    @Transactional(readOnly = true)
    public Page<Dtos.SessionSummary> list(Pageable pageable) {
        return sessionRepository
                .findByUserIdOrderByStartedAtDesc(currentUser.id(), pageable)
                .map(this::toSummary);
    }

    @Transactional(readOnly = true)
    public Dtos.SessionDetail get(UUID sessionId) {
        return toDetail(getSession(sessionId));
    }

    @Transactional
    public Dtos.SetResponse logSet(UUID sessionId, Dtos.LogSetRequest request) {
        WorkoutSession session = getSession(sessionId);

        if (session.getCompletedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Workout session is already completed");
        }

        Exercise exercise = exerciseRepository.findById(request.exerciseId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Exercise not found"));

        SessionSet set = SessionSet.builder()
                .session(session)
                .exercise(exercise)
                .setNumber(request.setNumber())
                .reps(request.reps())
                .weight(request.weight())
                .durationSeconds(request.durationSeconds())
                .distanceMeters(request.distanceMeters())
                .completed(request.completed() == null || request.completed())
                .build();

        return toSetResponse(setRepository.save(set));
    }

    @Transactional
    public Dtos.SessionDetail complete(UUID sessionId) {
        WorkoutSession session = getSession(sessionId);

        if (session.getCompletedAt() == null) {
            session.setCompletedAt(java.time.Instant.now());
            sessionRepository.save(session);
        }

        return toDetail(session);
    }

    @Transactional
    public void delete(UUID sessionId) {
        sessionRepository.delete(getSession(sessionId));
    }

    @Transactional(readOnly = true)
    public Dtos.SummaryResponse summary() {
        Object[] aggregate = setRepository.aggregateForUser(currentUser.id());
        long totalReps = ((Number) aggregate[0]).longValue();
        long totalSets = ((Number) aggregate[1]).longValue();

        return new Dtos.SummaryResponse(
                sessionRepository.countByUserId(currentUser.id()),
                totalSets,
                totalReps);
    }

    private WorkoutSession getSession(UUID sessionId) {
        return sessionRepository.findByIdAndUserId(sessionId, currentUser.id())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Workout session not found"));
    }

    private User getCurrentUser() {
        return userRepository.findById(currentUser.id())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Dtos.SessionSummary toSummary(WorkoutSession session) {
        return new Dtos.SessionSummary(
                session.getId(),
                session.getName(),
                session.getStartedAt(),
                session.getCompletedAt(),
                session.getSets().size());
    }

    private Dtos.SessionDetail toDetail(WorkoutSession session) {
        List<Dtos.SetResponse> sets = session.getSets().stream()
                .sorted(Comparator.comparing(SessionSet::getSetNumber))
                .map(this::toSetResponse)
                .toList();

        return new Dtos.SessionDetail(
                session.getId(),
                session.getName(),
                session.getStartedAt(),
                session.getCompletedAt(),
                session.getNotes(),
                sets);
    }

    private Dtos.SetResponse toSetResponse(SessionSet set) {
        return new Dtos.SetResponse(
                set.getId(),
                set.getExercise().getId(),
                set.getExercise().getName(),
                set.getSetNumber(),
                set.getReps(),
                set.getWeight(),
                set.getDurationSeconds(),
                set.getDistanceMeters(),
                set.getCompleted());
    }
}
