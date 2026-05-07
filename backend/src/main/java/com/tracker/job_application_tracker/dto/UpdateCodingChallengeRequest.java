package com.tracker.job_application_tracker.dto;

import com.tracker.job_application_tracker.enums.ChallengeStatus;
import com.tracker.job_application_tracker.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateCodingChallengeRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Platform is required")
    private String platform;

    @NotNull(message = "Difficulty is required")
    private Difficulty difficulty;

    @NotNull(message = "Status is required")
    private ChallengeStatus status;

    @Size(max = 1000, message = "Link must be at most 1000 characters")
    private String link;

    @Size(max = 5000, message = "Notes must be at most 5000 characters")
    private String notes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public ChallengeStatus getStatus() {
        return status;
    }

    public void setStatus(ChallengeStatus status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
