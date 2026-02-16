package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.dto.RegisterRequest;
import com.tracker.job_application_tracker.dto.UserDTO;
import com.tracker.job_application_tracker.model.User;

public interface UserService {

    User createUser(RegisterRequest request);

    User findByEmail(String email);

    void enableUser(String email);

    void updatePassword(String email, String newPassword);

    void verifyPassword(User user, String rawPassword);

    UserDTO convertToDTO(User user);
}
