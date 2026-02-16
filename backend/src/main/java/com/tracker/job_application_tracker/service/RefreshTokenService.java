package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.model.RefreshToken;
import com.tracker.job_application_tracker.model.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);

    void deleteByUser(User user);
}
