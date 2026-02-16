package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.dto.DocumentDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for Document management
 */
public interface DocumentService {
    
    /**
     * Get all documents for an application
     */
    List<DocumentDTO> getDocumentsByApplicationId(Long applicationId);
    
    /**
     * Upload a document for an application
     */
    DocumentDTO uploadDocument(Long applicationId, MultipartFile file);
    
    /**
     * Download a specific document
     */
    Resource downloadDocument(Long documentId);
    
    /**
     * Delete a specific document
     */
    void deleteDocument(Long documentId);
    
    /**
     * Delete all documents for an application
     */
    void deleteAllDocumentsByApplicationId(Long applicationId);
}