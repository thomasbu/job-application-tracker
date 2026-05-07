package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.dto.CodingChallengeDTO;
import com.tracker.job_application_tracker.dto.CreateCodingChallengeRequest;
import com.tracker.job_application_tracker.dto.UpdateCodingChallengeRequest;
import com.tracker.job_application_tracker.enums.ChallengeStatus;
import com.tracker.job_application_tracker.model.User;

import java.util.List;

public interface CodingChallengeService {

    List<CodingChallengeDTO> getAllByUser(User user);

    List<CodingChallengeDTO> getByStatus(User user, ChallengeStatus status);

    CodingChallengeDTO getById(Long id, User user);

    CodingChallengeDTO create(CreateCodingChallengeRequest request, User user);

    CodingChallengeDTO update(Long id, UpdateCodingChallengeRequest request, User user);

    void delete(Long id, User user);

    CodingChallengeDTO markCompleted(Long id, User user);

    Long countByStatus(User user, ChallengeStatus status);
}
