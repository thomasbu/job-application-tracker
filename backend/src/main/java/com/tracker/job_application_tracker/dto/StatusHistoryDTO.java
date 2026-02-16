package com.tracker.job_application_tracker.dto;

import com.tracker.job_application_tracker.enums.ApplicationStatus;
import java.time.LocalDateTime;

/**
 * DTO for StatusHistory entity
 */
public class StatusHistoryDTO {
    
    private Long id;
    private ApplicationStatus status;
    private LocalDateTime changedAt;
    private String notes;
    
    // === CONSTRUCTORS ===
    
    public StatusHistoryDTO() {
    }
    
    public StatusHistoryDTO(Long id, ApplicationStatus status, LocalDateTime changedAt, String notes) {
        this.id = id;
        this.status = status;
        this.changedAt = changedAt;
        this.notes = notes;
    }
    
    // === GETTERS AND SETTERS ===
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}