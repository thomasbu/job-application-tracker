package com.tracker.job_application_tracker.repository;

import com.tracker.job_application_tracker.enums.Category;
import com.tracker.job_application_tracker.model.FlashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard, Long> {

    List<FlashCard> findByUserIdOrderByLastReviewedAsc(Long userId);

    List<FlashCard> findByUserIdAndCategory(Long userId, Category category);

    Long countByUserId(Long userId);
}
