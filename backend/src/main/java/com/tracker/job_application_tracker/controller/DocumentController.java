package com.tracker.job_application_tracker.controller;

import com.tracker.job_application_tracker.dto.DocumentDTO;
import com.tracker.job_application_tracker.service.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST Controller for Document operations
 * 
 * Base URL: /api/applications/{applicationId}/documents
 */
@RestController
@RequestMapping("/api/applications/{applicationId}/documents")
public class DocumentController {
    
    private final DocumentService documentService;
    
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }
    
    /**
     * GET /api/applications/{applicationId}/documents
     * Get all documents for an application
     */
    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getDocuments(@PathVariable Long applicationId) {
        List<DocumentDTO> documents = documentService.getDocumentsByApplicationId(applicationId);
        return ResponseEntity.ok(documents);
    }
    
    /**
     * POST /api/applications/{applicationId}/documents
     * Upload a new document
     */
    @PostMapping
    public ResponseEntity<DocumentDTO> uploadDocument(
            @PathVariable Long applicationId,
            @RequestParam("file") MultipartFile file
    ) {
        DocumentDTO document = documentService.uploadDocument(applicationId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(document);
    }
    
    /**
     * GET /api/applications/{applicationId}/documents/{documentId}/download
     * Download a specific document
     */
    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long applicationId,
            @PathVariable Long documentId
    ) {
        Resource resource = documentService.downloadDocument(documentId);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    /**
     * DELETE /api/applications/{applicationId}/documents/{documentId}
     * Delete a specific document
     */
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long applicationId,
            @PathVariable Long documentId
    ) {
        documentService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}