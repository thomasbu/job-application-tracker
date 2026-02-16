package com.tracker.job_application_tracker.enums;

/**
 * Enum representing the different statuses of a job application
 * 
 * SENT = Application sent
 * INTERVIEW = Interview scheduled/obtained
 * REJECTED = Application rejected
 * ACCEPTED = Offer accepted
 */
public enum ApplicationStatus {
    SENT("Sent"),
    INTERVIEW("Interview"),
    REJECTED("Rejected"),
    ACCEPTED("Accepted");
    
    private final String displayName;
    
    // Enum constructor
    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }
    
    // Getter for display name
    public String getDisplayName() {
        return displayName;
    }
}