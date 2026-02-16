## 📎 SYSTÈME DE GESTION DE DOCUMENTS - DÉTAILS D'IMPLÉMENTATION

### Pourquoi cette feature ?
**USE CASE RÉEL** : En Belgique, les organismes comme ONEM/Actiris/VDAB exigent des preuves de recherche active d'emploi. Pouvoir joindre des documents (captures d'écran de postulation, emails de confirmation, etc.) est **essentiel** pour justifier tes démarches.

### Types de fichiers acceptés
- **PDF** : Captures d'écran converties, confirmations
- **Images** : JPG, PNG (screenshots de postulations)
- **Word** : DOC, DOCX (si lettres de motivation sauvegardées)
- **Taille max** : 10MB par fichier

### Flow utilisateur
1. **Lors de la création** : Option d'uploader un document
2. **Après postulation** : Peut ajouter/remplacer document
3. **Dans la liste** : Voir rapidement quelles postulations ont une preuve
4. **Télécharger** : Récupérer le document pour l'administration
5. **Supprimer** : Retirer si erreur ou plus nécessaire

### Implémentation Backend - FileStorageService

```java
@Service
public class FileStorageService {
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!");
        }
    }
    
    public String storeFile(MultipartFile file, Long applicationId) {
        // Validation
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        // Validate file type (security)
        String contentType = file.getContentType();
        List<String> allowedTypes = Arrays.asList(
            "application/pdf",
            "image/jpeg", "image/png", "image/jpg",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        );
        
        if (!allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("File type not allowed");
        }
        
        // Validate size (10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File too large");
        }
        
        // Generate unique filename
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = applicationId + "_" + timestamp + "_" + originalFilename;
        
        try {
            Path targetLocation = Paths.get(uploadDir).resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
    
    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found: " + filename);
            }
        } catch (MalformedURLException | FileNotFoundException e) {
            throw new RuntimeException("File not found: " + filename, e);
        }
    }
    
    public void deleteFile(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}
```

### Sécurité
- **Validation type MIME** : Uniquement PDF, images, Word
- **Taille limitée** : Max 10MB
- **Sanitization** : Nettoyage nom fichier (évite path traversal)
- **Stockage sécurisé** : Dossier uploads/ hors webroot
- **Nommage unique** : {appId}_{timestamp}_{originalName}


