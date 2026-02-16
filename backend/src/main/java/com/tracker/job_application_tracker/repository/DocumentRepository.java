package com.tracker.job_application_tracker.repository;

import com.tracker.job_application_tracker.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Document entity
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    /**
     * Find all documents for a specific application
     */
    List<Document> findByApplicationId(Long applicationId);
    
    /**
     * Delete all documents for a specific application
     */
    void deleteByApplicationId(Long applicationId);
}