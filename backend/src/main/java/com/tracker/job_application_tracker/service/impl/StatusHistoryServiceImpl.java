package com.tracker.job_application_tracker.service.impl;

import com.tracker.job_application_tracker.dto.StatusHistoryDTO;
import com.tracker.job_application_tracker.enums.ApplicationStatus;
import com.tracker.job_application_tracker.exception.ResourceNotFoundException;
import com.tracker.job_application_tracker.model.Application;
import com.tracker.job_application_tracker.model.StatusHistory;
import com.tracker.job_application_tracker.repository.ApplicationRepository;
import com.tracker.job_application_tracker.repository.StatusHistoryRepository;
import com.tracker.job_application_tracker.service.StatusHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of StatusHistoryService
 */
@Service
@Transactional
public class StatusHistoryServiceImpl implements StatusHistoryService {
    
    private final StatusHistoryRepository statusHistoryRepository;
    private final ApplicationRepository applicationRepository;
    
    public StatusHistoryServiceImpl(
            StatusHistoryRepository statusHistoryRepository,
            ApplicationRepository applicationRepository
    ) {
        this.statusHistoryRepository = statusHistoryRepository;
        this.applicationRepository = applicationRepository;
    }
    
    @Override
    public List<StatusHistoryDTO> getHistoryByApplicationId(Long applicationId) {
        // Verify application exists
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("Application", "id", applicationId);
        }
        
        return statusHistoryRepository.findByApplicationIdOrderByChangedAtDesc(applicationId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void createStatusHistory(Long applicationId, ApplicationStatus status, String notes) {
        // Find application
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));
        
        // Create history entry
        StatusHistory history = new StatusHistory(application, status, notes);
        
        // Save
        statusHistoryRepository.save(history);
    }
    
    // === HELPER METHOD ===
    
    private StatusHistoryDTO convertToDTO(StatusHistory history) {
        return new StatusHistoryDTO(
                history.getId(),
                history.getStatus(),
                history.getChangedAt(),
                history.getNotes()
        );
    }
}