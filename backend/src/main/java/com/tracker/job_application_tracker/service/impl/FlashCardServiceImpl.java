package com.tracker.job_application_tracker.service.impl;

import com.tracker.job_application_tracker.dto.CreateFlashCardRequest;
import com.tracker.job_application_tracker.dto.FlashCardDTO;
import com.tracker.job_application_tracker.dto.UpdateFlashCardRequest;
import com.tracker.job_application_tracker.enums.Category;
import com.tracker.job_application_tracker.exception.ResourceNotFoundException;
import com.tracker.job_application_tracker.model.FlashCard;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.repository.FlashCardRepository;
import com.tracker.job_application_tracker.service.FlashCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlashCardServiceImpl implements FlashCardService {

    private final FlashCardRepository flashCardRepository;

    public FlashCardServiceImpl(FlashCardRepository flashCardRepository) {
        this.flashCardRepository = flashCardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlashCardDTO> getAllByUser(User user) {
        return flashCardRepository.findByUserIdOrderByLastReviewedAsc(user.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlashCardDTO> getByCategory(User user, Category category) {
        return flashCardRepository.findByUserIdAndCategory(user.getId(), category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FlashCardDTO getById(Long id, User user) {
        FlashCard flashCard = flashCardRepository.findById(id)
                .filter(fc -> fc.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("FlashCard", "id", id));
        return convertToDTO(flashCard);
    }

    @Override
    public FlashCardDTO create(CreateFlashCardRequest request, User user) {
        FlashCard flashCard = new FlashCard();
        flashCard.setQuestion(request.getQuestion());
        flashCard.setAnswer(request.getAnswer());
        flashCard.setCategory(request.getCategory());
        flashCard.setDifficulty(request.getDifficulty());
        flashCard.setUser(user);

        FlashCard saved = flashCardRepository.save(flashCard);
        return convertToDTO(saved);
    }

    @Override
    public FlashCardDTO update(Long id, UpdateFlashCardRequest request, User user) {
        FlashCard flashCard = flashCardRepository.findById(id)
                .filter(fc -> fc.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("FlashCard", "id", id));

        flashCard.setQuestion(request.getQuestion());
        flashCard.setAnswer(request.getAnswer());
        flashCard.setCategory(request.getCategory());
        flashCard.setDifficulty(request.getDifficulty());
        flashCard.setConfidenceLevel(request.getConfidenceLevel());

        FlashCard saved = flashCardRepository.save(flashCard);
        return convertToDTO(saved);
    }

    @Override
    public void delete(Long id, User user) {
        FlashCard flashCard = flashCardRepository.findById(id)
                .filter(fc -> fc.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("FlashCard", "id", id));
        flashCardRepository.delete(flashCard);
    }

    @Override
    public FlashCardDTO markReviewed(Long id, int confidence, User user) {
        FlashCard flashCard = flashCardRepository.findById(id)
                .filter(fc -> fc.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("FlashCard", "id", id));

        flashCard.setLastReviewed(LocalDateTime.now());
        flashCard.setReviewCount(flashCard.getReviewCount() + 1);
        flashCard.setConfidenceLevel(confidence);

        FlashCard saved = flashCardRepository.save(flashCard);
        return convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByUser(User user) {
        return flashCardRepository.countByUserId(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Long countReviewedToday(User user) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        return flashCardRepository.findByUserIdOrderByLastReviewedAsc(user.getId())
                .stream()
                .filter(fc -> fc.getLastReviewed() != null && fc.getLastReviewed().isAfter(startOfDay))
                .count();
    }

    private FlashCardDTO convertToDTO(FlashCard flashCard) {
        FlashCardDTO dto = new FlashCardDTO();
        dto.setId(flashCard.getId());
        dto.setQuestion(flashCard.getQuestion());
        dto.setAnswer(flashCard.getAnswer());
        dto.setCategory(flashCard.getCategory());
        dto.setDifficulty(flashCard.getDifficulty());
        dto.setLastReviewed(flashCard.getLastReviewed());
        dto.setReviewCount(flashCard.getReviewCount());
        dto.setConfidenceLevel(flashCard.getConfidenceLevel());
        dto.setCreatedAt(flashCard.getCreatedAt());
        return dto;
    }
}
