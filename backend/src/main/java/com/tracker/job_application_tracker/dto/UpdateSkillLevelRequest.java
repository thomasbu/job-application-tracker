package com.tracker.job_application_tracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UpdateSkillLevelRequest {

    @Min(value = 0, message = "Level must be at least 0")
    @Max(value = 5, message = "Level must be at most 5")
    private int level;

    @Size(max = 2000, message = "Notes must be at most 2000 characters")
    private String notes;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
