package com.tracker.job_application_tracker.controller;

import com.tracker.job_application_tracker.dto.CreateFlashCardRequest;
import com.tracker.job_application_tracker.dto.FlashCardDTO;
import com.tracker.job_application_tracker.dto.UpdateFlashCardRequest;
import com.tracker.job_application_tracker.enums.Category;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.service.FlashCardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/flashcards")
public class FlashCardController {

    private final FlashCardService flashCardService;

    public FlashCardController(FlashCardService flashCardService) {
        this.flashCardService = flashCardService;
    }

    @GetMapping
    public ResponseEntity<List<FlashCardDTO>> getAllByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(flashCardService.getAllByUser(user));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<FlashCardDTO>> getByCategory(
            @PathVariable Category category,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(flashCardService.getByCategory(user, category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashCardDTO> getById(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(flashCardService.getById(id, user));
    }

    @PostMapping
    public ResponseEntity<FlashCardDTO> create(
            @Valid @RequestBody CreateFlashCardRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        FlashCardDTO created = flashCardService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashCardDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFlashCardRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(flashCardService.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        flashCardService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<FlashCardDTO> markReviewed(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        int confidence = body.getOrDefault("confidence", 0);
        return ResponseEntity.ok(flashCardService.markReviewed(id, confidence, user));
    }
}
