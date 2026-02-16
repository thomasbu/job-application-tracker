package com.tracker.job_application_tracker.repository;

import com.tracker.job_application_tracker.model.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for StatusHistory entity
 */
@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
    
    /**
     * Find all status history for a specific application
     * Ordered by changedAt DESC (most recent first)
     */
    List<StatusHistory> findByApplicationIdOrderByChangedAtDesc(Long applicationId);
}