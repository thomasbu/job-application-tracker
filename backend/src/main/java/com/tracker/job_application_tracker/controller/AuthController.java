package com.tracker.job_application_tracker.controller;

import com.tracker.job_application_tracker.dto.*;
import com.tracker.job_application_tracker.exception.TokenExpiredException;
import com.tracker.job_application_tracker.exception.UserNotEnabledException;
import com.tracker.job_application_tracker.model.ConfirmationToken;
import com.tracker.job_application_tracker.model.RefreshToken;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserService userService,
                          JwtService jwtService,
                          EmailService emailService,
                          ConfirmationTokenService confirmationTokenService,
                          RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.createUser(request);
        ConfirmationToken token = confirmationTokenService.createToken(user);
        emailService.sendConfirmationEmail(user.getEmail(), token.getToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Registration successful. Please check your email to confirm your account."));
    }

    @GetMapping("/confirm")
    public ResponseEntity<Map<String, String>> confirmEmail(@RequestParam String token) {
        confirmationTokenService.confirmToken(token);
        return ResponseEntity.ok(Map.of("message", "Email confirmed successfully. You can now log in."));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.findByEmail(request.getEmail());

        if (!user.isEnabled()) {
            throw new UserNotEnabledException(user.getEmail());
        }

        userService.verifyPassword(user, request.getPassword());

        String accessToken = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        AuthResponse response = new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                userService.convertToDTO(user)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());
        refreshToken = refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user.getEmail());

        AuthResponse response = new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                userService.convertToDTO(user)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        User user = userService.findByEmail(request.getEmail());
        ConfirmationToken token = confirmationTokenService.createToken(user);
        emailService.sendPasswordResetEmail(user.getEmail(), token.getToken());

        return ResponseEntity.ok(Map.of("message", "Password reset email sent."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        ConfirmationToken token = confirmationTokenService.getToken(request.getToken());

        if (token.getConfirmedAt() != null) {
            throw new com.tracker.job_application_tracker.exception.InvalidTokenException("Token already used");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token has expired");
        }

        userService.updatePassword(token.getUser().getEmail(), request.getNewPassword());
        confirmationTokenService.confirmToken(request.getToken());

        return ResponseEntity.ok(Map.of("message", "Password reset successfully."));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());
        refreshTokenService.deleteByUser(refreshToken.getUser());

        return ResponseEntity.ok(Map.of("message", "Logged out successfully."));
    }
}