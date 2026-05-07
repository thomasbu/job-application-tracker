package com.tracker.job_application_tracker.dto;

import com.tracker.job_application_tracker.enums.Category;
import com.tracker.job_application_tracker.enums.Difficulty;

import java.time.LocalDateTime;

public class FlashCardDTO {

    private Long id;
    private String question;
    private String answer;
    private Category category;
    private Difficulty difficulty;
    private LocalDateTime lastReviewed;
    private int reviewCount;
    private int confidenceLevel;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDateTime getLastReviewed() {
        return lastReviewed;
    }

    public void setLastReviewed(LocalDateTime lastReviewed) {
        this.lastReviewed = lastReviewed;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(int confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
