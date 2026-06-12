package com.tracker.job_application_tracker.controller;

import com.tracker.job_application_tracker.dto.ApplicationDTO;
import com.tracker.job_application_tracker.dto.CreateApplicationRequest;
import com.tracker.job_application_tracker.dto.UpdateApplicationRequest;
import com.tracker.job_application_tracker.enums.ApplicationStatus;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<Page<ApplicationDTO>> getAllApplications(
            @RequestParam(required = false) ApplicationStatus status,
            @PageableDefault(size = 20, sort = "applicationDate", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Page<ApplicationDTO> result;

        if (status != null) {
            result = applicationService.getApplicationsByStatus(user, status, pageable);
        } else {
            result = applicationService.getAllApplications(user, pageable);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDTO> getApplicationById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        ApplicationDTO application = applicationService.getApplicationById(user, id);
        return ResponseEntity.ok(application);
    }

    @PostMapping
    public ResponseEntity<ApplicationDTO> createApplication(
            @Valid @RequestBody CreateApplicationRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        ApplicationDTO created = applicationService.createApplication(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationDTO> updateApplication(
            @PathVariable Long id,
            @RequestBody UpdateApplicationRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        ApplicationDTO updated = applicationService.updateApplication(user, id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long id,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        applicationService.deleteApplication(user, id);
        return ResponseEntity.noContent().build();
    }
}
