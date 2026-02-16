package com.tracker.job_application_tracker.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.tracker.job_application_tracker.enums.ApplicationStatus;

/**
 * DTO for Application entity
 * Used to transfer data to the client (frontend)
 * 
 * Contains all fields that the frontend needs to display
 */
public class ApplicationDTO {
    
    private Long id;
    private String company;
    private String position;
    private LocalDate applicationDate;
    private ApplicationStatus currentStatus;
    private String notes;

    private List<DocumentDTO> documents;
    private int documentCount;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // === CONSTRUCTORS ===
    
    public ApplicationDTO() {
    }
    
    public ApplicationDTO(Long id, String company, String position, LocalDate applicationDate, 
                         ApplicationStatus currentStatus, String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.company = company;
        this.position = position;
        this.applicationDate = applicationDate;
        this.currentStatus = currentStatus;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // === GETTERS AND SETTERS ===
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public ApplicationStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(ApplicationStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<DocumentDTO> getDocuments() {
    return documents;
    }

    public void setDocuments(List<DocumentDTO> documents) {
        this.documents = documents;
    }

    public int getDocumentCount() {
        return documentCount;
    }

    public void setDocumentCount(int documentCount) {
        this.documentCount = documentCount;
    }

    // Status History (NEW)
    private List<StatusHistoryDTO> statusHistory;

    // Getter/Setter
    public List<StatusHistoryDTO> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<StatusHistoryDTO> statusHistory) {
        this.statusHistory = statusHistory;
    }
}