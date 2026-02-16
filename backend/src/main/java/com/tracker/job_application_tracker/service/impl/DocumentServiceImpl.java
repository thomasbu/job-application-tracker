package com.tracker.job_application_tracker.service.impl;

import com.tracker.job_application_tracker.dto.DocumentDTO;
import com.tracker.job_application_tracker.exception.ResourceNotFoundException;
import com.tracker.job_application_tracker.model.Application;
import com.tracker.job_application_tracker.model.Document;
import com.tracker.job_application_tracker.repository.ApplicationRepository;
import com.tracker.job_application_tracker.repository.DocumentRepository;
import com.tracker.job_application_tracker.service.DocumentService;
import com.tracker.job_application_tracker.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DocumentService
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {
    
    private final DocumentRepository documentRepository;
    private final ApplicationRepository applicationRepository;
    private final FileStorageService fileStorageService;
    
    public DocumentServiceImpl(
            DocumentRepository documentRepository,
            ApplicationRepository applicationRepository,
            FileStorageService fileStorageService
    ) {
        this.documentRepository = documentRepository;
        this.applicationRepository = applicationRepository;
        this.fileStorageService = fileStorageService;
    }
    
    @Override
    public List<DocumentDTO> getDocumentsByApplicationId(Long applicationId) {
        // Verify application exists
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("Application", "id", applicationId);
        }
        
        return documentRepository.findByApplicationId(applicationId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public DocumentDTO uploadDocument(Long applicationId, MultipartFile file) {
        // Find application
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));
        
        // Store file
        String storedFilename = fileStorageService.storeFile(file, applicationId);
        
        // Create document entity
        Document document = new Document(
                application,
                file.getOriginalFilename(),
                storedFilename,
                file.getContentType(),
                file.getSize()
        );
        
        // Save document
        Document savedDocument = documentRepository.save(document);
        
        return convertToDTO(savedDocument);
    }
    
    @Override
    public Resource downloadDocument(Long documentId) {
        // Find document
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", documentId));
        
        // Load file as resource
        return fileStorageService.loadFileAsResource(document.getStoredFilename());
    }
    
    @Override
    public void deleteDocument(Long documentId) {
        // Find document
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document", "id", documentId));
        
        // Delete file from storage
        fileStorageService.deleteFile(document.getStoredFilename());
        
        // Delete document from database
        documentRepository.delete(document);
    }
    
    @Override
    public void deleteAllDocumentsByApplicationId(Long applicationId) {
        List<Document> documents = documentRepository.findByApplicationId(applicationId);
        
        // Delete all files
        documents.forEach(doc -> fileStorageService.deleteFile(doc.getStoredFilename()));
        
        // Delete all documents from database
        documentRepository.deleteByApplicationId(applicationId);
    }
    
    // === HELPER METHOD ===
    
    private DocumentDTO convertToDTO(Document document) {
        return new DocumentDTO(
                document.getId(),
                document.getOriginalFilename(),
                document.getContentType(),
                document.getFileSize(),
                document.getUploadedAt()
        );
    }
}