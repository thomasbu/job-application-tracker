package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.dto.StatusHistoryDTO;
import com.tracker.job_application_tracker.enums.ApplicationStatus;

import java.util.List;

/**
 * Service interface for StatusHistory management
 */
public interface StatusHistoryService {
    
    /**
     * Get all status history for an application
     */
    List<StatusHistoryDTO> getHistoryByApplicationId(Long applicationId);
    
    /**
     * Create a new status history entry
     */
    void createStatusHistory(Long applicationId, ApplicationStatus status, String notes);
}