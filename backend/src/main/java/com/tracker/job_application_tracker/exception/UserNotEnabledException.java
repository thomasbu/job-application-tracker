package com.tracker.job_application_tracker.exception;

public class UserNotEnabledException extends RuntimeException {

    public UserNotEnabledException(String email) {
        super("User account is not enabled: " + email);
    }
}
