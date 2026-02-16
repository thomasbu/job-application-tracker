package com.tracker.job_application_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import com.tracker.job_application_tracker.enums.ApplicationStatus;

/**
 * DTO for creating a new application
 * Contains only the fields needed to create an application
 * 
 * Uses validation annotations to ensure data integrity
 */
public class CreateApplicationRequest {
    
    @NotBlank(message = "Company name is required")
    private String company;
    
    @NotBlank(message = "Position is required")
    private String position;
    
    @NotNull(message = "Application date is required")
    private LocalDate applicationDate;
    
    @NotNull(message = "Status is required")
    private ApplicationStatus currentStatus;
    
    private String notes; // Optional field
    
    // === CONSTRUCTORS ===
    
    public CreateApplicationRequest() {
    }
    
    public CreateApplicationRequest(String company, String position, LocalDate applicationDate, 
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