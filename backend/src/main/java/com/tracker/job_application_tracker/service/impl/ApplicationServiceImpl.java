package com.tracker.job_application_tracker.service.impl;

import com.tracker.job_application_tracker.dto.ApplicationDTO;
import com.tracker.job_application_tracker.dto.CreateApplicationRequest;
import com.tracker.job_application_tracker.dto.DocumentDTO;
import com.tracker.job_application_tracker.dto.StatusHistoryDTO;
import com.tracker.job_application_tracker.dto.UpdateApplicationRequest;
import com.tracker.job_application_tracker.enums.ApplicationStatus;
import com.tracker.job_application_tracker.exception.ResourceNotFoundException;
import com.tracker.job_application_tracker.model.Application;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.repository.ApplicationRepository;
import com.tracker.job_application_tracker.service.ApplicationService;
import com.tracker.job_application_tracker.service.DocumentService;
import com.tracker.job_application_tracker.service.StatusHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final DocumentService documentService;
    private final StatusHistoryService statusHistoryService;

    public ApplicationServiceImpl(
            ApplicationRepository applicationRepository,
            DocumentService documentService,
            StatusHistoryService statusHistoryService
    ) {
        this.applicationRepository = applicationRepository;
        this.documentService = documentService;
        this.statusHistoryService = statusHistoryService;
    }

    @Override
    public List<ApplicationDTO> getAllApplications(User user) {
        return applicationRepository.findByUserIdOrderByApplicationDateDesc(user.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationDTO> getApplicationsByStatus(User user, ApplicationStatus status) {
        return applicationRepository.findByUserIdAndCurrentStatus(user.getId(), status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationDTO getApplicationById(User user, Long id) {
        Application application = applicationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));
        return convertToDTO(application);
    }

    @Override
    public ApplicationDTO createApplication(User user, CreateApplicationRequest request) {
        Application application = new Application();
        application.setUser(user);
        application.setCompany(request.getCompany());
        application.setPosition(request.getPosition());
        application.setApplicationDate(request.getApplicationDate());
        application.setCurrentStatus(request.getCurrentStatus());
        application.setNotes(request.getNotes());

        Application savedApplication = applicationRepository.save(application);

        statusHistoryService.createStatusHistory(
                savedApplication.getId(),
                savedApplication.getCurrentStatus(),
                "Application created"
        );

        return convertToDTO(savedApplication);
    }

    @Override
    public ApplicationDTO updateApplication(User user, Long id, UpdateApplicationRequest request) {
        Application application = applicationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));

        ApplicationStatus oldStatus = application.getCurrentStatus();
        boolean statusChanged = false;

        if (request.getCompany() != null) {
            application.setCompany(request.getCompany());
        }
        if (request.getPosition() != null) {
            application.setPosition(request.getPosition());
        }
        if (request.getApplicationDate() != null) {
            application.setApplicationDate(request.getApplicationDate());
        }
        if (request.getCurrentStatus() != null && request.getCurrentStatus() != oldStatus) {
            application.setCurrentStatus(request.getCurrentStatus());
            statusChanged = true;
        }
        if (request.getNotes() != null) {
            application.setNotes(request.getNotes());
        }

        Application updatedApplication = applicationRepository.save(application);

        if (statusChanged) {
            statusHistoryService.createStatusHistory(
                    updatedApplication.getId(),
                    updatedApplication.getCurrentStatus(),
                    "Status changed from " + oldStatus + " to " + updatedApplication.getCurrentStatus()
            );
        }

        return convertToDTO(updatedApplication);
    }

    @Override
    public void deleteApplication(User user, Long id) {
        applicationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));

        documentService.deleteAllDocumentsByApplicationId(id);
        applicationRepository.deleteById(id);
    }

    // === HELPER METHOD ===

    private ApplicationDTO convertToDTO(Application application) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());
        dto.setCompany(application.getCompany());
        dto.setPosition(application.getPosition());
        dto.setApplicationDate(application.getApplicationDate());
        dto.setCurrentStatus(application.getCurrentStatus());
        dto.setNotes(application.getNotes());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setUpdatedAt(application.getUpdatedAt());

        List<DocumentDTO> documents = documentService.getDocumentsByApplicationId(application.getId());
        dto.setDocuments(documents);
        dto.setDocumentCount(documents.size());

        List<StatusHistoryDTO> history = statusHistoryService.getHistoryByApplicationId(application.getId());
        dto.setStatusHistory(history);

        return dto;
    }
}
