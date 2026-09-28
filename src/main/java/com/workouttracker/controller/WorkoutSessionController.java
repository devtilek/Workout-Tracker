package com.workouttracker.controller;

import com.workouttracker.dto.Dtos;
import com.workouttracker.service.WorkoutSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class WorkoutSessionController {
    private final WorkoutSessionService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dtos.SessionDetail start(
            @Valid @RequestBody Dtos.StartSessionRequest request) {
        return service.start(request);
    }

    @GetMapping
    public Page<Dtos.SessionSummary> list(Pageable pageable) {
        return service.list(pageable);
    }

    @GetMapping("/summary")
    public Dtos.SummaryResponse summary() {
        return service.summary();
    }

    @GetMapping("/{sessionId}")
    public Dtos.SessionDetail get(@PathVariable UUID sessionId) {
        return service.get(sessionId);
    }

    @PostMapping("/{sessionId}/sets")
    public Dtos.SetResponse logSet(
            @PathVariable UUID sessionId,
            @Valid @RequestBody Dtos.LogSetRequest request) {
        return service.logSet(sessionId, request);
    }

    @PostMapping("/{sessionId}/complete")
    public Dtos.SessionDetail complete(@PathVariable UUID sessionId) {
        return service.complete(sessionId);
    }

    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID sessionId) {
        service.delete(sessionId);
    }
}
