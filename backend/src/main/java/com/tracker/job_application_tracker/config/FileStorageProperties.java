package com.tracker.job_application_tracker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for file storage
 * Reads values from application.properties (file.upload-dir)
 */
@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    
    private String uploadDir = "./uploads";
    
    public String getUploadDir() {
        return uploadDir;
    }
    
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}