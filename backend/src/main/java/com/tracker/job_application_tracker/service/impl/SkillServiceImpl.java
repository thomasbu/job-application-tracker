package com.tracker.job_application_tracker.service.impl;

import com.tracker.job_application_tracker.dto.CreateSkillRequest;
import com.tracker.job_application_tracker.dto.SkillDTO;
import com.tracker.job_application_tracker.dto.UpdateSkillLevelRequest;
import com.tracker.job_application_tracker.exception.ResourceNotFoundException;
import com.tracker.job_application_tracker.model.Skill;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.repository.SkillRepository;
import com.tracker.job_application_tracker.service.SkillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDTO> getAllByUser(User user) {
        return skillRepository.findByUserIdOrderByCategoryAsc(user.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SkillDTO getById(Long id, User user) {
        Skill skill = skillRepository.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));
        return convertToDTO(skill);
    }

    @Override
    public SkillDTO create(CreateSkillRequest request, User user) {
        Skill skill = new Skill();
        skill.setName(request.getName());
        skill.setCategory(request.getCategory());
        skill.setUser(user);

        Skill saved = skillRepository.save(skill);
        return convertToDTO(saved);
    }

    @Override
    public SkillDTO updateLevel(Long id, UpdateSkillLevelRequest request, User user) {
        Skill skill = skillRepository.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));

        skill.setLevel(request.getLevel());
        skill.setNotes(request.getNotes());

        Skill saved = skillRepository.save(skill);
        return convertToDTO(saved);
    }

    @Override
    public void delete(Long id, User user) {
        Skill skill = skillRepository.findByUserIdAndId(user.getId(), id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));
        skillRepository.delete(skill);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageLevel(User user) {
        List<Skill> skills = skillRepository.findByUserIdOrderByCategoryAsc(user.getId());
        if (skills.isEmpty()) {
            return 0.0;
        }
        return skills.stream()
                .mapToInt(Skill::getLevel)
                .average()
                .orElse(0.0);
    }

    private SkillDTO convertToDTO(Skill skill) {
        SkillDTO dto = new SkillDTO();
        dto.setId(skill.getId());
        dto.setName(skill.getName());
        dto.setCategory(skill.getCategory());
        dto.setLevel(skill.getLevel());
        dto.setNotes(skill.getNotes());
        dto.setUpdatedAt(skill.getUpdatedAt());
        return dto;
    }
}
