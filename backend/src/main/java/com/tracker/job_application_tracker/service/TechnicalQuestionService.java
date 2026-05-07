package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.dto.CreateTechnicalQuestionRequest;
import com.tracker.job_application_tracker.dto.TechnicalQuestionDTO;
import com.tracker.job_application_tracker.dto.UpdateTechnicalQuestionRequest;
import com.tracker.job_application_tracker.enums.Category;
import com.tracker.job_application_tracker.model.User;

import java.util.List;

public interface TechnicalQuestionService {

    List<TechnicalQuestionDTO> getAllByUser(User user);

    List<TechnicalQuestionDTO> getByCategory(User user, Category category);

    TechnicalQuestionDTO getById(Long id, User user);

    TechnicalQuestionDTO create(CreateTechnicalQuestionRequest request, User user);

    TechnicalQuestionDTO update(Long id, UpdateTechnicalQuestionRequest request, User user);

    void delete(Long id, User user);

    TechnicalQuestionDTO updateConfidence(Long id, int confidence, User user);
}
