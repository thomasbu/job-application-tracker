package com.tracker.job_application_tracker.service.impl;

import com.tracker.job_application_tracker.dto.CodingChallengeDTO;
import com.tracker.job_application_tracker.dto.CreateCodingChallengeRequest;
import com.tracker.job_application_tracker.dto.UpdateCodingChallengeRequest;
import com.tracker.job_application_tracker.enums.ChallengeStatus;
import com.tracker.job_application_tracker.exception.ResourceNotFoundException;
import com.tracker.job_application_tracker.model.CodingChallenge;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.repository.CodingChallengeRepository;
import com.tracker.job_application_tracker.service.CodingChallengeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CodingChallengeServiceImpl implements CodingChallengeService {

    private final CodingChallengeRepository codingChallengeRepository;

    public CodingChallengeServiceImpl(CodingChallengeRepository codingChallengeRepository) {
        this.codingChallengeRepository = codingChallengeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingChallengeDTO> getAllByUser(User user) {
        return codingChallengeRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingChallengeDTO> getByStatus(User user, ChallengeStatus status) {
        return codingChallengeRepository.findByUserIdAndStatus(user.getId(), status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CodingChallengeDTO getById(Long id, User user) {
        CodingChallenge challenge = codingChallengeRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("CodingChallenge", "id", id));
        return convertToDTO(challenge);
    }

    @Override
    public CodingChallengeDTO create(CreateCodingChallengeRequest request, User user) {
        CodingChallenge challenge = new CodingChallenge();
        challenge.setName(request.getName());
        challenge.setPlatform(request.getPlatform());
        challenge.setDifficulty(request.getDifficulty());
        challenge.setLink(request.getLink());
        challenge.setNotes(request.getNotes());
        challenge.setUser(user);

        CodingChallenge saved = codingChallengeRepository.save(challenge);
        return convertToDTO(saved);
    }

    @Override
    public CodingChallengeDTO update(Long id, UpdateCodingChallengeRequest request, User user) {
        CodingChallenge challenge = codingChallengeRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("CodingChallenge", "id", id));

        challenge.setName(request.getName());
        challenge.setPlatform(request.getPlatform());
        challenge.setDifficulty(request.getDifficulty());
        challenge.setStatus(request.getStatus());
        challenge.setLink(request.getLink());
        challenge.setNotes(request.getNotes());

        if (request.getStatus() == ChallengeStatus.COMPLETED && challenge.getCompletedAt() == null) {
            challenge.setCompletedAt(LocalDateTime.now());
        }

        CodingChallenge saved = codingChallengeRepository.save(challenge);
        return convertToDTO(saved);
    }

    @Override
    public void delete(Long id, User user) {
        CodingChallenge challenge = codingChallengeRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("CodingChallenge", "id", id));
        codingChallengeRepository.delete(challenge);
    }

    @Override
    public CodingChallengeDTO markCompleted(Long id, User user) {
        CodingChallenge challenge = codingChallengeRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("CodingChallenge", "id", id));

        challenge.setStatus(ChallengeStatus.COMPLETED);
        challenge.setCompletedAt(LocalDateTime.now());

        CodingChallenge saved = codingChallengeRepository.save(challenge);
        return convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByStatus(User user, ChallengeStatus status) {
        return codingChallengeRepository.countByUserIdAndStatus(user.getId(), status);
    }

    private CodingChallengeDTO convertToDTO(CodingChallenge challenge) {
        CodingChallengeDTO dto = new CodingChallengeDTO();
        dto.setId(challenge.getId());
        dto.setName(challenge.getName());
        dto.setPlatform(challenge.getPlatform());
        dto.setDifficulty(challenge.getDifficulty());
        dto.setStatus(challenge.getStatus());
        dto.setLink(challenge.getLink());
        dto.setNotes(challenge.getNotes());
        dto.setCompletedAt(challenge.getCompletedAt());
        dto.setCreatedAt(challenge.getCreatedAt());
        return dto;
    }
}
