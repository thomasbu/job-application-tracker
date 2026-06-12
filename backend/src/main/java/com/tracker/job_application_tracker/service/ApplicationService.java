package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.dto.ApplicationDTO;
import com.tracker.job_application_tracker.dto.CreateApplicationRequest;
import com.tracker.job_application_tracker.dto.UpdateApplicationRequest;
import com.tracker.job_application_tracker.enums.ApplicationStatus;
import com.tracker.job_application_tracker.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApplicationService {

    List<ApplicationDTO> getAllApplications(User user);

    List<ApplicationDTO> getApplicationsByStatus(User user, ApplicationStatus status);

    Page<ApplicationDTO> getAllApplications(User user, Pageable pageable);

    Page<ApplicationDTO> getApplicationsByStatus(User user, ApplicationStatus status, Pageable pageable);

    ApplicationDTO getApplicationById(User user, Long id);

    ApplicationDTO createApplication(User user, CreateApplicationRequest request);

    ApplicationDTO updateApplication(User user, Long id, UpdateApplicationRequest request);

    void deleteApplication(User user, Long id);
}
