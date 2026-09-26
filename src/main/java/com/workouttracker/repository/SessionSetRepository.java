package com.workouttracker.repository;

import com.workouttracker.domain.SessionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface SessionSetRepository extends JpaRepository<SessionSet, UUID> {
    @Query("SELECT s FROM SessionSet s JOIN FETCH s.exercise JOIN FETCH s.session " +
            "WHERE s.session.user.id = :userId AND s.exercise.id = :exerciseId " +
            "ORDER BY s.session.startedAt ASC")
    List<SessionSet> findForProgress(@Param("userId") UUID userId,
                                     @Param("exerciseId") UUID exerciseId);

    @Query("SELECT s FROM SessionSet s JOIN FETCH s.exercise JOIN FETCH s.session " +
            "WHERE s.session.user.id = :userId AND s.weight IS NOT NULL")
    List<SessionSet> findAllWithWeightForUser(@Param("userId") UUID userId);

    @Query("SELECT COALESCE(SUM(s.reps), 0), COUNT(s) FROM SessionSet s " +
            "WHERE s.session.user.id = :userId AND s.completed = true")
    Object[] aggregateForUser(@Param("userId") UUID userId);
}
