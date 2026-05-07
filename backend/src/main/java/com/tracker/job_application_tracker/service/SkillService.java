package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.dto.CreateSkillRequest;
import com.tracker.job_application_tracker.dto.SkillDTO;
import com.tracker.job_application_tracker.dto.UpdateSkillLevelRequest;
import com.tracker.job_application_tracker.model.User;

import java.util.List;

public interface SkillService {

    List<SkillDTO> getAllByUser(User user);

    SkillDTO getById(Long id, User user);

    SkillDTO create(CreateSkillRequest request, User user);

    SkillDTO updateLevel(Long id, UpdateSkillLevelRequest request, User user);

    void delete(Long id, User user);

    Double getAverageLevel(User user);
}
