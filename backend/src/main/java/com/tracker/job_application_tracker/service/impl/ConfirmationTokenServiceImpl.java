package com.tracker.job_application_tracker.service.impl;

import com.tracker.job_application_tracker.exception.InvalidTokenException;
import com.tracker.job_application_tracker.exception.TokenExpiredException;
import com.tracker.job_application_tracker.model.ConfirmationToken;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.repository.ConfirmationTokenRepository;
import com.tracker.job_application_tracker.service.ConfirmationTokenService;
import com.tracker.job_application_tracker.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository tokenRepository;
    private final UserService userService;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository tokenRepository, UserService userService) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }

    @Override
    public ConfirmationToken createToken(User user) {
        String tokenString = UUID.randomUUID().toString();
        ConfirmationToken token = new ConfirmationToken(
                tokenString,
                LocalDateTime.now().plusMinutes(15),
                user
        );
        return tokenRepository.save(token);
    }

    @Override
    @Transactional(readOnly = true)
    public ConfirmationToken getToken(String tokenString) {
        return tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
    }

    @Override
    public void confirmToken(String tokenString) {
        ConfirmationToken token = getToken(tokenString);

        if (token.getConfirmedAt() != null) {
            throw new InvalidTokenException("Token already confirmed");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token has expired");
        }

        token.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(token);

        userService.enableUser(token.getUser().getEmail());
    }
}
