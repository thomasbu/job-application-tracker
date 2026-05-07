package com.tracker.job_application_tracker.dto;

import com.tracker.job_application_tracker.enums.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CreateStudySessionRequest {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Topic is required")
    private Category topic;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private int durationMinutes;

    @Size(max = 2000, message = "Notes must be at most 2000 characters")
    private String notes;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Category getTopic() {
        return topic;
    }

    public void setTopic(Category topic) {
        this.topic = topic;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
