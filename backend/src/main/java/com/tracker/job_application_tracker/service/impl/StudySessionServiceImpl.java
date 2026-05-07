package com.tracker.job_application_tracker.service.impl;

import com.tracker.job_application_tracker.dto.CreateStudySessionRequest;
import com.tracker.job_application_tracker.dto.StudySessionDTO;
import com.tracker.job_application_tracker.exception.ResourceNotFoundException;
import com.tracker.job_application_tracker.model.StudySession;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.repository.StudySessionRepository;
import com.tracker.job_application_tracker.service.StudySessionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudySessionServiceImpl implements StudySessionService {

    private final StudySessionRepository studySessionRepository;

    public StudySessionServiceImpl(StudySessionRepository studySessionRepository) {
        this.studySessionRepository = studySessionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudySessionDTO> getAllByUser(User user) {
        return studySessionRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudySessionDTO> getByDateRange(User user, LocalDate start, LocalDate end) {
        return studySessionRepository.findByUserIdAndDateBetween(user.getId(), start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudySessionDTO getById(Long id, User user) {
        StudySession session = studySessionRepository.findById(id)
                .filter(s -> s.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("StudySession", "id", id));
        return convertToDTO(session);
    }

    @Override
    public StudySessionDTO create(CreateStudySessionRequest request, User user) {
        StudySession session = new StudySession();
        session.setDate(request.getDate());
        session.setTopic(request.getTopic());
        session.setDurationMinutes(request.getDurationMinutes());
        session.setNotes(request.getNotes());
        session.setUser(user);

        StudySession saved = studySessionRepository.save(session);
        return convertToDTO(saved);
    }

    @Override
    public StudySessionDTO update(Long id, CreateStudySessionRequest request, User user) {
        StudySession session = studySessionRepository.findById(id)
                .filter(s -> s.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("StudySession", "id", id));

        session.setDate(request.getDate());
        session.setTopic(request.getTopic());
        session.setDurationMinutes(request.getDurationMinutes());
        session.setNotes(request.getNotes());

        StudySession saved = studySessionRepository.save(session);
        return convertToDTO(saved);
    }

    @Override
    public void delete(Long id, User user) {
        StudySession session = studySessionRepository.findById(id)
                .filter(s -> s.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("StudySession", "id", id));
        studySessionRepository.delete(session);
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalMinutesThisWeek(User user) {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return studySessionRepository.findByUserIdAndDateBetween(user.getId(), startOfWeek, endOfWeek)
                .stream()
                .mapToInt(StudySession::getDurationMinutes)
                .sum();
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalMinutesThisMonth(User user) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth());

        return studySessionRepository.findByUserIdAndDateBetween(user.getId(), startOfMonth, endOfMonth)
                .stream()
                .mapToInt(StudySession::getDurationMinutes)
                .sum();
    }

    private StudySessionDTO convertToDTO(StudySession session) {
        StudySessionDTO dto = new StudySessionDTO();
        dto.setId(session.getId());
        dto.setDate(session.getDate());
        dto.setTopic(session.getTopic());
        dto.setDurationMinutes(session.getDurationMinutes());
        dto.setNotes(session.getNotes());
        dto.setCreatedAt(session.getCreatedAt());
        return dto;
    }
}
