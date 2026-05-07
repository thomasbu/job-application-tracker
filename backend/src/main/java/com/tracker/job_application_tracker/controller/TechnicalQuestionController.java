package com.tracker.job_application_tracker.controller;

import com.tracker.job_application_tracker.dto.CreateTechnicalQuestionRequest;
import com.tracker.job_application_tracker.dto.TechnicalQuestionDTO;
import com.tracker.job_application_tracker.dto.UpdateTechnicalQuestionRequest;
import com.tracker.job_application_tracker.enums.Category;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.service.TechnicalQuestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class TechnicalQuestionController {

    private final TechnicalQuestionService technicalQuestionService;

    public TechnicalQuestionController(TechnicalQuestionService technicalQuestionService) {
        this.technicalQuestionService = technicalQuestionService;
    }

    @GetMapping
    public ResponseEntity<List<TechnicalQuestionDTO>> getAllByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(technicalQuestionService.getAllByUser(user));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<TechnicalQuestionDTO>> getByCategory(
            @PathVariable Category category,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(technicalQuestionService.getByCategory(user, category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicalQuestionDTO> getById(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(technicalQuestionService.getById(id, user));
    }

    @PostMapping
    public ResponseEntity<TechnicalQuestionDTO> create(
            @Valid @RequestBody CreateTechnicalQuestionRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        TechnicalQuestionDTO created = technicalQuestionService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnicalQuestionDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTechnicalQuestionRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(technicalQuestionService.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        technicalQuestionService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/confidence")
    public ResponseEntity<TechnicalQuestionDTO> updateConfidence(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        int confidence = body.getOrDefault("confidence", 0);
        return ResponseEntity.ok(technicalQuestionService.updateConfidence(id, confidence, user));
    }
}
