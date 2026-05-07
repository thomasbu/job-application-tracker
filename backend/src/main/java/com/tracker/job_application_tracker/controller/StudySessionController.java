package com.tracker.job_application_tracker.controller;

import com.tracker.job_application_tracker.dto.CreateStudySessionRequest;
import com.tracker.job_application_tracker.dto.StudySessionDTO;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.service.StudySessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-sessions")
public class StudySessionController {

    private final StudySessionService studySessionService;

    public StudySessionController(StudySessionService studySessionService) {
        this.studySessionService = studySessionService;
    }

    @GetMapping
    public ResponseEntity<List<StudySessionDTO>> getAllByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(studySessionService.getAllByUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudySessionDTO> getById(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(studySessionService.getById(id, user));
    }

    @PostMapping
    public ResponseEntity<StudySessionDTO> create(
            @Valid @RequestBody CreateStudySessionRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        StudySessionDTO created = studySessionService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudySessionDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateStudySessionRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(studySessionService.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        studySessionService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/week")
    public ResponseEntity<Integer> getTotalMinutesThisWeek(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(studySessionService.getTotalMinutesThisWeek(user));
    }

    @GetMapping("/stats/month")
    public ResponseEntity<Integer> getTotalMinutesThisMonth(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(studySessionService.getTotalMinutesThisMonth(user));
    }
}
