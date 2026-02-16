package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.dto.ApplicationDTO;
import com.tracker.job_application_tracker.dto.CreateApplicationRequest;
import com.tracker.job_application_tracker.dto.UpdateApplicationRequest;
import com.tracker.job_application_tracker.enums.ApplicationStatus;
import com.tracker.job_application_tracker.model.User;

import java.util.List;

public interface ApplicationService {

    List<ApplicationDTO> getAllApplications(User user);

    List<ApplicationDTO> getApplicationsByStatus(User user, ApplicationStatus status);

    ApplicationDTO getApplicationById(User user, Long id);

    ApplicationDTO createApplication(User user, CreateApplicationRequest request);

    ApplicationDTO updateApplication(User user, Long id, UpdateApplicationRequest request);

    void deleteApplication(User user, Long id);
}
