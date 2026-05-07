package com.tracker.job_application_tracker.service.impl;

import com.tracker.job_application_tracker.dto.CreateTechnicalQuestionRequest;
import com.tracker.job_application_tracker.dto.TechnicalQuestionDTO;
import com.tracker.job_application_tracker.dto.UpdateTechnicalQuestionRequest;
import com.tracker.job_application_tracker.enums.Category;
import com.tracker.job_application_tracker.exception.ResourceNotFoundException;
import com.tracker.job_application_tracker.model.TechnicalQuestion;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.repository.TechnicalQuestionRepository;
import com.tracker.job_application_tracker.service.TechnicalQuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TechnicalQuestionServiceImpl implements TechnicalQuestionService {

    private final TechnicalQuestionRepository technicalQuestionRepository;

    public TechnicalQuestionServiceImpl(TechnicalQuestionRepository technicalQuestionRepository) {
        this.technicalQuestionRepository = technicalQuestionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicalQuestionDTO> getAllByUser(User user) {
        return technicalQuestionRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicalQuestionDTO> getByCategory(User user, Category category) {
        return technicalQuestionRepository.findByUserIdAndCategory(user.getId(), category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TechnicalQuestionDTO getById(Long id, User user) {
        TechnicalQuestion question = technicalQuestionRepository.findById(id)
                .filter(q -> q.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("TechnicalQuestion", "id", id));
        return convertToDTO(question);
    }

    @Override
    public TechnicalQuestionDTO create(CreateTechnicalQuestionRequest request, User user) {
        TechnicalQuestion question = new TechnicalQuestion();
        question.setQuestion(request.getQuestion());
        question.setAnswer(request.getAnswer());
        question.setCategory(request.getCategory());
        question.setUser(user);

        TechnicalQuestion saved = technicalQuestionRepository.save(question);
        return convertToDTO(saved);
    }

    @Override
    public TechnicalQuestionDTO update(Long id, UpdateTechnicalQuestionRequest request, User user) {
        TechnicalQuestion question = technicalQuestionRepository.findById(id)
                .filter(q -> q.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("TechnicalQuestion", "id", id));

        question.setQuestion(request.getQuestion());
        question.setAnswer(request.getAnswer());
        question.setCategory(request.getCategory());
        question.setConfidenceLevel(request.getConfidenceLevel());

        TechnicalQuestion saved = technicalQuestionRepository.save(question);
        return convertToDTO(saved);
    }

    @Override
    public void delete(Long id, User user) {
        TechnicalQuestion question = technicalQuestionRepository.findById(id)
                .filter(q -> q.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("TechnicalQuestion", "id", id));
        technicalQuestionRepository.delete(question);
    }

    @Override
    public TechnicalQuestionDTO updateConfidence(Long id, int confidence, User user) {
        TechnicalQuestion question = technicalQuestionRepository.findById(id)
                .filter(q -> q.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("TechnicalQuestion", "id", id));

        question.setConfidenceLevel(confidence);
        question.setLastReviewed(LocalDateTime.now());

        TechnicalQuestion saved = technicalQuestionRepository.save(question);
        return convertToDTO(saved);
    }

    private TechnicalQuestionDTO convertToDTO(TechnicalQuestion question) {
        TechnicalQuestionDTO dto = new TechnicalQuestionDTO();
        dto.setId(question.getId());
        dto.setQuestion(question.getQuestion());
        dto.setAnswer(question.getAnswer());
        dto.setCategory(question.getCategory());
        dto.setConfidenceLevel(question.getConfidenceLevel());
        dto.setLastReviewed(question.getLastReviewed());
        dto.setCreatedAt(question.getCreatedAt());
        return dto;
    }
}
