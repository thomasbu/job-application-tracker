package com.tracker.job_application_tracker.model;

import com.tracker.job_application_tracker.enums.ApplicationStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing the history of status changes for an application
 * Tracks when and what status changes occurred
 */
@Entity
@Table(name = "status_history")
public class StatusHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ApplicationStatus status;
    
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    // === HIBERNATE CALLBACK ===
    
    @PrePersist
    protected void onCreate() {
        if (changedAt == null) {
            changedAt = LocalDateTime.now();
        }
    }
    
    // === CONSTRUCTORS ===
    
    public StatusHistory() {
    }
    
    public StatusHistory(Application application, ApplicationStatus status, String notes) {
        this.application = application;
        this.status = status;
        this.notes = notes;
    }
    
    // === GETTERS AND SETTERS ===
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
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