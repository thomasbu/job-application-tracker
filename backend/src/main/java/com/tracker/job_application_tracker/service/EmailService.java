package com.tracker.job_application_tracker.service;

public interface EmailService {

    void sendConfirmationEmail(String to, String token);

    void sendPasswordResetEmail(String to, String token);
}
