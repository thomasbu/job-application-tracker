package com.tracker.job_application_tracker.dto;

import com.tracker.job_application_tracker.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateSkillRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Category is required")
    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
