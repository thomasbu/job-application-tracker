package com.tracker.job_application_tracker.repository;

import com.tracker.job_application_tracker.enums.ApplicationStatus;
import com.tracker.job_application_tracker.model.Application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Application entity
 * 
 * JpaRepository provides out-of-the-box CRUD operations:
 * - save(entity)
 * - findById(id)
 * - findAll()
 * - deleteById(id)
 * - count()
 * - etc.
 * 
 * We can also add custom query methods using naming conventions
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    /**
     * Find all applications with a specific status
     * Spring Data JPA automatically generates the query from the method name!
     * 
     * Generated SQL: SELECT * FROM applications WHERE current_status = ?
     */
    List<Application> findByCurrentStatus(ApplicationStatus status);
    
    /**
     * Find all applications ordered by application date descending (most recent first)
     * 
     * Generated SQL: SELECT * FROM applications ORDER BY application_date DESC
     */
    List<Application> findAllByOrderByApplicationDateDesc();
    
    /**
     * Find applications by company name (case-insensitive)
     * 
     * Generated SQL: SELECT * FROM applications WHERE LOWER(company) LIKE LOWER(?)
     */
    List<Application> findByCompanyContainingIgnoreCase(String company);

    List<Application> findByUserIdOrderByApplicationDateDesc(Long userId);

    List<Application> findByUserIdAndCurrentStatus(Long userId, ApplicationStatus status);

    Optional<Application> findByIdAndUserId(Long id, Long userId);
}