# Technical Specifications - Job Application Tracker

## 🏗️ Architecture Détaillée

### Backend Architecture

#### 1. Package Structure
```
com.tracker.jobapplication/
├── JobApplicationTrackerApplication.java (main)
├── controller/
│   └── ApplicationController.java
├── service/
│   ├── ApplicationService.java (interface)
│   └── ApplicationServiceImpl.java
├── repository/
│   └── ApplicationRepository.java
├── model/
│   ├── Application.java (entity)
│   └── ApplicationStatus.java (enum)
├── dto/
│   ├── ApplicationDTO.java
│   ├── CreateApplicationRequest.java
│   └── UpdateApplicationRequest.java
├── exception/
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
└── config/
    └── CorsConfig.java
```

#### 2. Entity: Application

```java
@Entity
@Table(name = "applications")
public class Application {
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
    
    // 🆕 Document justificatif fields
    @Column(name = "document_file_name")
    private String documentFileName;
    
    @Column(name = "document_path")
    private String documentPath;
    
    @Column(name = "document_content_type")
    private String documentContentType;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // 🆕 Helper method
    public boolean hasDocument() {
        return documentFileName != null && !documentFileName.isEmpty();
    }
}
```

#### 3. Enum: ApplicationStatus

```java
public enum ApplicationStatus {
    SENT("Envoyé"),
    INTERVIEW("Entretien"),
    REJECTED("Refusé"),
    ACCEPTED("Accepté");
    
    private final String displayName;
    
    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
```

#### 4. Repository

```java
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByCurrentStatus(ApplicationStatus status);
    List<Application> findAllByOrderByApplicationDateDesc();
}
```

#### 5. Service Interface

```java
public interface ApplicationService {
    List<ApplicationDTO> getAllApplications();
    List<ApplicationDTO> getApplicationsByStatus(ApplicationStatus status);
    ApplicationDTO getApplicationById(Long id);
    ApplicationDTO createApplication(CreateApplicationRequest request);
    ApplicationDTO updateApplication(Long id, UpdateApplicationRequest request);
    void deleteApplication(Long id);
    
    // 🆕 Document management methods
    ApplicationDTO uploadDocument(Long id, MultipartFile file);
    Resource downloadDocument(Long id);
    void deleteDocument(Long id);
}
```

#### 6. Controller Endpoints

```java
@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200")
public class ApplicationController {
    
    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getAllApplications(
        @RequestParam(required = false) ApplicationStatus status
    );
    
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable Long id);
    
    @PostMapping
    public ResponseEntity<ApplicationDTO> createApplication(
        @Valid @RequestBody CreateApplicationRequest request
    );
    
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationDTO> updateApplication(
        @PathVariable Long id,
        @Valid @RequestBody UpdateApplicationRequest request
    );
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id);
    
    // 🆕 Document endpoints
    @PostMapping("/{id}/document")
    public ResponseEntity<ApplicationDTO> uploadDocument(
        @PathVariable Long id,
        @RequestParam("file") MultipartFile file
    );
    
    @GetMapping("/{id}/document")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id);
    
    @DeleteMapping("/{id}/document")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id);
}
```

#### 7. DTOs

```java
// ApplicationDTO.java
public class ApplicationDTO {
    private Long id;
    private String company;
    private String position;
    private LocalDate applicationDate;
    private ApplicationStatus currentStatus;
    private String notes;
    private String documentFileName; // 🆕
    private boolean hasDocument;     // 🆕
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// CreateApplicationRequest.java
public class CreateApplicationRequest {
    @NotBlank(message = "Company is required")
    private String company;
    
    @NotBlank(message = "Position is required")
    private String position;
    
    @NotNull(message = "Application date is required")
    private LocalDate applicationDate;
    
    @NotNull(message = "Status is required")
    private ApplicationStatus currentStatus;
    
    private String notes;
}

// UpdateApplicationRequest.java
public class UpdateApplicationRequest {
    private String company;
    private String position;
    private LocalDate applicationDate;
    private ApplicationStatus currentStatus;
    private String notes;
}
```

#### 8. Exception Handling

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
        ResourceNotFoundException ex
    ) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
        MethodArgumentNotValidException ex
    ) {
        // Handle validation errors
    }
}
```

#### 9. Configuration

**application.properties:**
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/job_tracker
spring.datasource.username=tracker_user
spring.datasource.password=tracker_pass
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server
server.port=8080

# Jackson
spring.jackson.serialization.write-dates-as-timestamps=false

# 🆕 File Upload Configuration
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
file.upload-dir=./uploads
```

---

### Frontend Architecture

#### 1. Project Structure
```
src/app/
├── models/
│   ├── application.model.ts
│   └── application-status.enum.ts
├── services/
│   └── application.service.ts
├── components/
│   ├── application-list/
│   │   ├── application-list.component.ts
│   │   ├── application-list.component.html
│   │   └── application-list.component.scss
│   └── application-dialog/
│       ├── application-dialog.component.ts
│       ├── application-dialog.component.html
│       └── application-dialog.component.scss
├── app.component.ts
├── app.config.ts
└── app.routes.ts
```

#### 2. Models

**application.model.ts:**
```typescript
export interface Application {
  id?: number;
  company: string;
  position: string;
  applicationDate: string;
  currentStatus: ApplicationStatus;
  notes?: string;
  documentFileName?: string;  // 🆕
  hasDocument?: boolean;       // 🆕
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateApplicationRequest {
  company: string;
  position: string;
  applicationDate: string;
  currentStatus: ApplicationStatus;
  notes?: string;
}
```

**application-status.enum.ts:**
```typescript
export enum ApplicationStatus {
  SENT = 'SENT',
  INTERVIEW = 'INTERVIEW',
  REJECTED = 'REJECTED',
  ACCEPTED = 'ACCEPTED'
}

export const APPLICATION_STATUS_LABELS = {
  [ApplicationStatus.SENT]: 'Envoyé',
  [ApplicationStatus.INTERVIEW]: 'Entretien',
  [ApplicationStatus.REJECTED]: 'Refusé',
  [ApplicationStatus.ACCEPTED]: 'Accepté'
};
```

#### 3. Service

**application.service.ts:**
```typescript
@Injectable({
  providedIn: 'root'
})
export class ApplicationService {
  private apiUrl = 'http://localhost:8080/api/applications';

  constructor(private http: HttpClient) {}

  getAllApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(this.apiUrl);
  }

  getApplicationsByStatus(status: ApplicationStatus): Observable<Application[]> {
    return this.http.get<Application[]>(`${this.apiUrl}?status=${status}`);
  }

  getApplicationById(id: number): Observable<Application> {
    return this.http.get<Application>(`${this.apiUrl}/${id}`);
  }

  createApplication(request: CreateApplicationRequest): Observable<Application> {
    return this.http.post<Application>(this.apiUrl, request);
  }

  updateApplication(id: number, request: Partial<Application>): Observable<Application> {
    return this.http.put<Application>(`${this.apiUrl}/${id}`, request);
  }

  deleteApplication(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // 🆕 Document management methods
  uploadDocument(id: number, file: File): Observable<Application> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Application>(`${this.apiUrl}/${id}/document`, formData);
  }

  downloadDocument(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/document`, { 
      responseType: 'blob' 
    });
  }

  deleteDocument(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}/document`);
  }
}
```

#### 4. Components

**application-list.component.ts - Key Features:**
- Material Table avec colonnes: Company, Position, Date, Status, **Document** 🆕, Actions
- Filtres par statut (dropdown Material)
- Bouton "Nouvelle postulation"
- **Icône/badge pour indiquer si document présent** 🆕
- Actions: Edit (dialog), Delete (confirmation), **Download document**, **Delete document** 🆕
- Refresh automatique après modifications

**application-dialog.component.ts - Key Features:**
- Reactive Form avec validations
- Champs: company, position, applicationDate (datepicker), status (select), notes (textarea)
- **Input file upload (accept: .pdf,.jpg,.jpeg,.png,.doc,.docx)** 🆕
- **Preview nom fichier sélectionné** 🆕
- **Upload après création/édition de la postulation** 🆕
- Mode création vs édition
- Annuler / Sauvegarder

#### 5. Material Modules Nécessaires

```typescript
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBarModule } from '@angular/material/snack-bar';
```

---

## 🐳 Docker Configuration

**docker-compose.yml:**
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: job-tracker-db
    environment:
      POSTGRES_DB: job_tracker
      POSTGRES_USER: tracker_user
      POSTGRES_PASSWORD: tracker_pass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  # 🆕 Backend service (optionnel pour dev, utile pour prod)
  backend:
    build: ./backend
    container_name: job-tracker-backend
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/job_tracker
      SPRING_DATASOURCE_USERNAME: tracker_user
      SPRING_DATASOURCE_PASSWORD: tracker_pass
    volumes:
      - uploads_data:/app/uploads  # 🆕 Persistance des fichiers uploadés

volumes:
  postgres_data:
  uploads_data:  # 🆕
```

---

## 📝 Dependencies

### Backend (pom.xml)
```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Spring Boot Starter Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok (optionnel mais recommandé) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

### Frontend (package.json)
```json
{
  "dependencies": {
    "@angular/animations": "^18.0.0",
    "@angular/common": "^18.0.0",
    "@angular/core": "^18.0.0",
    "@angular/forms": "^18.0.0",
    "@angular/material": "^18.0.0",
    "@angular/platform-browser": "^18.0.0",
    "@angular/router": "^18.0.0",
    "rxjs": "~7.8.0"
  }
}
```

---

## 🧪 Tests Manuels (MVP)

### Backend Tests (Postman/curl)

```bash
# Create
curl -X POST http://localhost:8080/api/applications \
  -H "Content-Type: application/json" \
  -d '{"company":"Google","position":"Developer","applicationDate":"2025-02-12","currentStatus":"SENT"}'

# Get All
curl http://localhost:8080/api/applications

# Get By Status
curl http://localhost:8080/api/applications?status=SENT

# Get By ID
curl http://localhost:8080/api/applications/1

# Update
curl -X PUT http://localhost:8080/api/applications/1 \
  -H "Content-Type: application/json" \
  -d '{"currentStatus":"INTERVIEW"}'

# 🆕 Upload Document
curl -X POST http://localhost:8080/api/applications/1/document \
  -F "file=@/path/to/document.pdf"

# 🆕 Download Document
curl http://localhost:8080/api/applications/1/document \
  --output document.pdf

# 🆕 Delete Document
curl -X DELETE http://localhost:8080/api/applications/1/document

# Delete Application
curl -X DELETE http://localhost:8080/api/applications/1
```

---

## ✅ Definition of Done (MVP)

**Backend:**
- [ ] Tous les endpoints fonctionnels
- [ ] Validation des données
- [ ] Gestion erreurs (404, 400, 500)
- [ ] **Upload fichier fonctionne (max 10MB)** 🆕
- [ ] **Stockage sécurisé dans /uploads** 🆕
- [ ] **Download fichier avec bon Content-Type** 🆕
- [ ] **Suppression fichier du disque quand delete** 🆕
- [ ] CORS configuré
- [ ] Code propre et commenté

**Frontend:**
- [ ] Liste affiche toutes les postulations
- [ ] **Indicateur visuel si document présent** 🆕
- [ ] Filtrage par statut fonctionne
- [ ] Création de postulation via dialog
- [ ] **Upload de document dans dialog** 🆕
- [ ] **Bouton télécharger document si présent** 🆕
- [ ] **Bouton supprimer document** 🆕
- [ ] Édition de postulation
- [ ] Suppression avec confirmation
- [ ] Messages de succès/erreur (snackbar)
- [ ] UI responsive

**DevOps:**
- [ ] PostgreSQL démarre via Docker Compose
- [ ] **Volume uploads persistant** 🆕
- [ ] README avec instructions claires
- [ ] Repo GitHub avec commits réguliers

**Bonus:**
- [ ] Tri par date dans le tableau
- [ ] Compteur de postulations par statut
- [ ] Loading spinners
- [ ] **Prévisualisation document (si image/PDF)** 🆕
- [ ] **Types de fichiers autorisés clairement indiqués** 🆕
