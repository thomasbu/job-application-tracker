package com.tracker.job_application_tracker.repository;

import com.tracker.job_application_tracker.model.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findByUserIdOrderByDateDesc(Long userId);

    List<StudySession> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
}
