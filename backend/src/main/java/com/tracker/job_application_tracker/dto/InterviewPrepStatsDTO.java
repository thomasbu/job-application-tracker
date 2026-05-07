package com.tracker.job_application_tracker.dto;

public class InterviewPrepStatsDTO {

    private Long totalFlashCards;
    private Long cardsReviewedToday;
    private Long challengesCompleted;
    private Long challengesTotal;
    private int totalStudyTimeThisWeek;
    private double averageSkillLevel;

    public Long getTotalFlashCards() {
        return totalFlashCards;
    }

    public void setTotalFlashCards(Long totalFlashCards) {
        this.totalFlashCards = totalFlashCards;
    }

    public Long getCardsReviewedToday() {
        return cardsReviewedToday;
    }

    public void setCardsReviewedToday(Long cardsReviewedToday) {
        this.cardsReviewedToday = cardsReviewedToday;
    }

    public Long getChallengesCompleted() {
        return challengesCompleted;
    }

    public void setChallengesCompleted(Long challengesCompleted) {
        this.challengesCompleted = challengesCompleted;
    }

    public Long getChallengesTotal() {
        return challengesTotal;
    }

    public void setChallengesTotal(Long challengesTotal) {
        this.challengesTotal = challengesTotal;
    }

    public int getTotalStudyTimeThisWeek() {
        return totalStudyTimeThisWeek;
    }

    public void setTotalStudyTimeThisWeek(int totalStudyTimeThisWeek) {
        this.totalStudyTimeThisWeek = totalStudyTimeThisWeek;
    }

    public double getAverageSkillLevel() {
        return averageSkillLevel;
    }

    public void setAverageSkillLevel(double averageSkillLevel) {
        this.averageSkillLevel = averageSkillLevel;
    }
}
