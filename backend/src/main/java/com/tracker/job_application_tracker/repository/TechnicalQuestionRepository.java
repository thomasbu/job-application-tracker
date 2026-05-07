package com.tracker.job_application_tracker.repository;

import com.tracker.job_application_tracker.enums.Category;
import com.tracker.job_application_tracker.model.TechnicalQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnicalQuestionRepository extends JpaRepository<TechnicalQuestion, Long> {

    List<TechnicalQuestion> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<TechnicalQuestion> findByUserIdAndCategory(Long userId, Category category);
}
