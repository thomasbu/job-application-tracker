package com.tracker.job_application_tracker.controller;

import com.tracker.job_application_tracker.dto.CodingChallengeDTO;
import com.tracker.job_application_tracker.dto.CreateCodingChallengeRequest;
import com.tracker.job_application_tracker.dto.UpdateCodingChallengeRequest;
import com.tracker.job_application_tracker.enums.ChallengeStatus;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.service.CodingChallengeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
public class CodingChallengeController {

    private final CodingChallengeService codingChallengeService;

    public CodingChallengeController(CodingChallengeService codingChallengeService) {
        this.codingChallengeService = codingChallengeService;
    }

    @GetMapping
    public ResponseEntity<List<CodingChallengeDTO>> getAllByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(codingChallengeService.getAllByUser(user));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CodingChallengeDTO>> getByStatus(
            @PathVariable ChallengeStatus status,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(codingChallengeService.getByStatus(user, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CodingChallengeDTO> getById(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(codingChallengeService.getById(id, user));
    }

    @PostMapping
    public ResponseEntity<CodingChallengeDTO> create(
            @Valid @RequestBody CreateCodingChallengeRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CodingChallengeDTO created = codingChallengeService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CodingChallengeDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCodingChallengeRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(codingChallengeService.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        codingChallengeService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<CodingChallengeDTO> markCompleted(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(codingChallengeService.markCompleted(id, user));
    }
}
