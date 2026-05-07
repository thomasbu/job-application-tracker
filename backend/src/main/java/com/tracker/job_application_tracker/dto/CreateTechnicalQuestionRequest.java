package com.tracker.job_application_tracker.dto;

import com.tracker.job_application_tracker.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateTechnicalQuestionRequest {

    @NotBlank(message = "Question is required")
    @Size(max = 1000, message = "Question must be at most 1000 characters")
    private String question;

    @NotBlank(message = "Answer is required")
    @Size(max = 5000, message = "Answer must be at most 5000 characters")
    private String answer;

    @NotNull(message = "Category is required")
    private Category category;

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
}
