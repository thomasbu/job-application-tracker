package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.dto.CreateFlashCardRequest;
import com.tracker.job_application_tracker.dto.FlashCardDTO;
import com.tracker.job_application_tracker.dto.UpdateFlashCardRequest;
import com.tracker.job_application_tracker.enums.Category;
import com.tracker.job_application_tracker.model.User;

import java.util.List;

public interface FlashCardService {

    List<FlashCardDTO> getAllByUser(User user);

    List<FlashCardDTO> getByCategory(User user, Category category);

    FlashCardDTO getById(Long id, User user);

    FlashCardDTO create(CreateFlashCardRequest request, User user);

    FlashCardDTO update(Long id, UpdateFlashCardRequest request, User user);

    void delete(Long id, User user);

    FlashCardDTO markReviewed(Long id, int confidence, User user);

    Long countByUser(User user);

    Long countReviewedToday(User user);
}
