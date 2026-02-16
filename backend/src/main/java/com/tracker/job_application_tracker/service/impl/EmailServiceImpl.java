package com.tracker.job_application_tracker.service.impl;

import com.tracker.job_application_tracker.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendConfirmationEmail(String to, String token) {
        String confirmUrl = frontendUrl + "/auth/confirm?token=" + token;
        String subject = "Confirm your email";
        String htmlContent = "<html><body>"
                + "<h2>Welcome to Job Application Tracker!</h2>"
                + "<p>Please click the link below to confirm your email:</p>"
                + "<a href=\"" + confirmUrl + "\">Confirm Email</a>"
                + "<p>This link will expire in 15 minutes.</p>"
                + "</body></html>";

        sendHtmlEmail(to, subject, htmlContent);
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        String subject = "Reset your password";
        String htmlContent = "<html><body>"
                + "<h2>Password Reset Request</h2>"
                + "<p>Click the link below to reset your password:</p>"
                + "<a href=\"" + resetUrl + "\">Reset Password</a>"
                + "<p>This link will expire in 15 minutes.</p>"
                + "<p>If you did not request this, please ignore this email.</p>"
                + "</body></html>";

        sendHtmlEmail(to, subject, htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
