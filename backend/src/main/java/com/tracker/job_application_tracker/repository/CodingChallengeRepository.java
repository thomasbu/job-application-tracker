package com.tracker.job_application_tracker.repository;

import com.tracker.job_application_tracker.enums.ChallengeStatus;
import com.tracker.job_application_tracker.model.CodingChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodingChallengeRepository extends JpaRepository<CodingChallenge, Long> {

    List<CodingChallenge> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<CodingChallenge> findByUserIdAndStatus(Long userId, ChallengeStatus status);

    Long countByUserIdAndStatus(Long userId, ChallengeStatus status);
}
