package com.workouttracker.service;

import com.workouttracker.domain.User;
import com.workouttracker.domain.WorkoutSession;
import com.workouttracker.dto.Dtos;
import com.workouttracker.repository.ExerciseRepository;
import com.workouttracker.repository.SessionSetRepository;
import com.workouttracker.repository.UserRepository;
import com.workouttracker.repository.WorkoutPlanRepository;
import com.workouttracker.repository.WorkoutSessionRepository;
import com.workouttracker.security.CurrentUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutSessionServiceTest {

    @Mock private WorkoutSessionRepository sessionRepository;
    @Mock private SessionSetRepository setRepository;
    @Mock private ExerciseRepository exerciseRepository;
    @Mock private WorkoutPlanRepository planRepository;
    @Mock private UserRepository userRepository;
    @Mock private CurrentUser currentUser;

    @InjectMocks private WorkoutSessionService service;

    @Test
    void shouldRejectLoggingSetAfterSessionCompleted() {
        UUID userId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();

        WorkoutSession session = WorkoutSession.builder()
                .id(sessionId)
                .user(User.builder().id(userId).build())
                .completedAt(Instant.now())
                .sets(new ArrayList<>())
                .build();

        when(currentUser.id()).thenReturn(userId);
        when(sessionRepository.findByIdAndUserId(sessionId, userId))
                .thenReturn(java.util.Optional.of(session));

        Dtos.LogSetRequest request = new Dtos.LogSetRequest(
                UUID.randomUUID(), 1, 10, null, null, null, true);

        assertThatThrownBy(() -> service.logSet(sessionId, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Workout session is already completed");

        verifyNoInteractions(exerciseRepository, setRepository);
    }
}
