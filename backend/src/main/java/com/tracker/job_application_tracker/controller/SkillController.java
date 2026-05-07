package com.tracker.job_application_tracker.controller;

import com.tracker.job_application_tracker.dto.CreateSkillRequest;
import com.tracker.job_application_tracker.dto.SkillDTO;
import com.tracker.job_application_tracker.dto.UpdateSkillLevelRequest;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<SkillDTO>> getAllByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(skillService.getAllByUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDTO> getById(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(skillService.getById(id, user));
    }

    @PostMapping
    public ResponseEntity<SkillDTO> create(
            @Valid @RequestBody CreateSkillRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        SkillDTO created = skillService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/level")
    public ResponseEntity<SkillDTO> updateLevel(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSkillLevelRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(skillService.updateLevel(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        skillService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
