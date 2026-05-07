package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.dto.CreateStudySessionRequest;
import com.tracker.job_application_tracker.dto.StudySessionDTO;
import com.tracker.job_application_tracker.model.User;

import java.time.LocalDate;
import java.util.List;

public interface StudySessionService {

    List<StudySessionDTO> getAllByUser(User user);

    List<StudySessionDTO> getByDateRange(User user, LocalDate start, LocalDate end);

    StudySessionDTO getById(Long id, User user);

    StudySessionDTO create(CreateStudySessionRequest request, User user);

    StudySessionDTO update(Long id, CreateStudySessionRequest request, User user);

    void delete(Long id, User user);

    int getTotalMinutesThisWeek(User user);

    int getTotalMinutesThisMonth(User user);
}
