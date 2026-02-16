package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.config.FileStorageProperties;
import com.tracker.job_application_tracker.exception.FileStorageException;
import com.tracker.job_application_tracker.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

/**
 * Service for handling file storage operations
 * - Upload files
 * - Download files
 * - Delete files
 * - File validation
 */
@Service
public class FileStorageService {
    
    private final Path fileStorageLocation;
    
    // Allowed file types (MIME types)
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "image/jpg",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    
    // Maximum file size: 10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    
    /**
     * Constructor - initializes file storage location
     */
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath()
                .normalize();
    }
    
    /**
     * Create upload directory if it doesn't exist
     * Called after bean construction
     */
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create upload directory!", ex);
        }
    }
    
    /**
     * Store a file for a specific application
     * 
     * @param file The file to upload
     * @param applicationId The ID of the application
     * @return The stored filename
     */
    public String storeFile(MultipartFile file, Long applicationId) {
        // Validate file is not empty
        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file");
        }
        
        // Validate file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileStorageException("File size exceeds maximum limit of 10MB");
        }
        
        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new FileStorageException("File type not allowed. Allowed types: PDF, JPG, PNG, DOC, DOCX");
        }
        
        // Sanitize filename
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        
        // Check if filename contains invalid characters
        if (originalFilename.contains("..")) {
            throw new FileStorageException("Filename contains invalid path sequence: " + originalFilename);
        }
        
        // Generate unique filename: {applicationId}_{timestamp}_{originalName}
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = applicationId + "_" + timestamp + "_" + originalFilename;
        
        try {
            // Copy file to target location
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return filename;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + filename, ex);
        }
    }
    
    /**
     * Load a file as a Resource
     * 
     * @param filename The filename to load
     * @return Resource object containing the file
     */
    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found: " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File not found: " + filename);
        }
    }
    
    /**
     * Delete a file
     * 
     * @param filename The filename to delete
     */
    public void deleteFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            return; // Nothing to delete
        }
        
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file " + filename, ex);
        }
    }
}