package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.model.ConfirmationToken;
import com.tracker.job_application_tracker.model.User;

public interface ConfirmationTokenService {

    ConfirmationToken createToken(User user);

    ConfirmationToken getToken(String token);

    void confirmToken(String token);
}
