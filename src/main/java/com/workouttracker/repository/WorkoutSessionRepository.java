package com.workouttracker.repository;

import com.workouttracker.domain.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, UUID> {
    Page<WorkoutSession> findByUserIdOrderByStartedAtDesc(UUID userId, Pageable pageable);
    Optional<WorkoutSession> findByIdAndUserId(UUID id, UUID userId);

    long countByUserId(UUID userId);
}
