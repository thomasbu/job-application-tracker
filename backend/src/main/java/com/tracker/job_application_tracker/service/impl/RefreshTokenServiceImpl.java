package com.tracker.job_application_tracker.service.impl;

import com.tracker.job_application_tracker.exception.InvalidTokenException;
import com.tracker.job_application_tracker.exception.TokenExpiredException;
import com.tracker.job_application_tracker.model.RefreshToken;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.repository.RefreshTokenRepository;
import com.tracker.job_application_tracker.service.RefreshTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository tokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(User user) {
        tokenRepository.deleteByUser(user);

        RefreshToken token = new RefreshToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusDays(7),
                user
        );

        return tokenRepository.save(token);
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken findByToken(String tokenString) {
        return tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(token);
            throw new TokenExpiredException("Refresh token has expired. Please log in again.");
        }
        return token;
    }

    @Override
    public void deleteByUser(User user) {
        tokenRepository.deleteByUser(user);
    }
}
