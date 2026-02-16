package com.tracker.job_application_tracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tracker.job_application_tracker.enums.ApplicationStatus;

/**
 * Entity representing a job application
 * This class will be automatically transformed into a MySQL table by Hibernate
 * 
 * Table: applications
 */
@Entity
@Table(name = "applications")
public class Application {
    
    // === TABLE COLUMNS ===
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String company;
    
    @Column(nullable = false)
    private String position;
    
    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    private ApplicationStatus currentStatus;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    // === AUTOMATIC TIMESTAMPS ===
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // === HIBERNATE CALLBACKS ===
    
    /**
     * Called automatically BEFORE inserting into database
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Called automatically BEFORE each update in database
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // === CONSTRUCTORS ===
    
    /**
     * Default constructor (required by JPA)
     */
    public Application() {
    }
    
    /**
     * Constructor with required fields
     */
    public Application(String company, String position, LocalDate applicationDate, ApplicationStatus currentStatus) {
        this.company = company;
        this.position = position;
        this.applicationDate = applicationDate;
        this.currentStatus = currentStatus;
    }
    
    // === GETTERS AND SETTERS ===
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public ApplicationStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(ApplicationStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // === RELATION WITH USER ===

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // === RELATION WITH DOCUMENTS ===

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    // === HELPER METHODS FOR DOCUMENTS ===

    public void addDocument(Document document) {
        documents.add(document);
        document.setApplication(this);
    }

    public void removeDocument(Document document) {
        documents.remove(document);
        document.setApplication(null);
    }

    public boolean hasDocuments() {
        return documents != null && !documents.isEmpty();
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocument(List<Document> documents) {
        this.documents = documents;
    }

    // === RELATION WITH STATUS HISTORY ===

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("changedAt DESC")
    private List<StatusHistory> statusHistory = new ArrayList<>();

    // === HELPER METHODS FOR STATUS HISTORY ===

    public void addStatusHistory(StatusHistory history) {
        statusHistory.add(history);
        history.setApplication(this);
    }

    // Getter/Setter
    public List<StatusHistory> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<StatusHistory> statusHistory) {
        this.statusHistory = statusHistory;
    }
}