package com.tracker.job_application_tracker.dto;

import java.time.LocalDate;

import com.tracker.job_application_tracker.enums.ApplicationStatus;

/**
 * DTO for updating an existing application
 * All fields are optional (partial updates allowed)
 * 
 * Only the fields that are provided will be updated
 */
public class UpdateApplicationRequest {
    
    private String company;
    private String position;
    private LocalDate applicationDate;
    private ApplicationStatus currentStatus;
    private String notes;
    
    // === CONSTRUCTORS ===
    
    public UpdateApplicationRequest() {
    }
    
    public UpdateApplicationRequest(String company, String position, LocalDate applicationDate, 
                                   ApplicationStatus currentStatus, String notes) {
        this.company = company;
        this.position = position;
        this.applicationDate = applicationDate;
        this.currentStatus = currentStatus;
        this.notes = notes;
    }
    
    // === GETTERS AND SETTERS ===
    
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
}