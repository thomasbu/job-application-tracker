# Job Application Tracker - Project Overview

## 🎯 Objectif
Application full-stack pour suivre ses candidatures avec historique des changements de statut.

## ⚡ Contexte
- **Timeline**: Sprint intensif 2-3 jours (J1 = MVP, J2-J3 = enrichissement)
- **But**: Projet portfolio pour démontrer maîtrise stack Java/Angular
- **Usage réel**: Outil personnel pour gérer ses postulations

## 🛠️ Stack Technique

### Backend
- **Framework**: Spring Boot 3.x
- **Java**: Version 17 ou 21
- **BDD**: PostgreSQL 15+
- **ORM**: JPA/Hibernate
- **Build**: Maven
- **API**: REST JSON

### Frontend
- **Framework**: Angular 17 ou 18
- **UI**: Angular Material
- **HTTP**: HttpClient
- **Routing**: Angular Router

### Infrastructure
- **Container**: Docker Compose
- **Version Control**: Git/GitHub

## 📋 Features

### MVP (Jour 1) ✅
1. **CRUD Postulations complètes**
   - Créer une postulation
   - Lister toutes les postulations
   - Modifier une postulation
   - Supprimer une postulation

2. **Upload de documents justificatifs** 🆕
   - Télécharger document (PDF, image, Word)
   - Associer document à une postulation
   - Visualiser/télécharger le document
   - Supprimer document
   - **USE CASE** : Prouver aux administrations (ONEM, Actiris) qu'on a postulé

3. **Filtrage basique**
   - Filtrer par statut (envoyé, entretien, refusé, accepté)

4. **Interface utilisateur**
   - Tableau avec liste des postulations + indicateur document
   - Dialog pour ajouter/éditer avec upload
   - Actions (éditer, supprimer, voir document)

### Phase 2 (Jour 2-3) 🔄
4. **Historique des statuts**
   - Table relationnelle pour tracer les changements
   - Timeline dans vue détail

5. **Vue détail enrichie**
   - Page dédiée par postulation
   - Historique visuel (timeline Material)

6. **Statistiques**
   - Dashboard avec compteurs par statut
   - Graphiques simples

### Option B (si temps) 🚀
7. **Authentification**
   - Spring Security + JWT
   - Login/Register

8. **Features avancées**
   - Pagination
   - Recherche/tri avancé
   - Export PDF/Excel
   - Tests unitaires
   - CI/CD GitHub Actions

## 🗂️ Structure Projet

```
job-application-tracker/
├── backend/
│   ├── src/main/java/com/tracker/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   └── config/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── src/app/
│   │   ├── components/
│   │   ├── services/
│   │   ├── models/
│   │   └── app.routes.ts
│   ├── package.json
│   └── Dockerfile
├── docker-compose.yml
└── README.md
```

## 🎨 Design Patterns

### Backend
- **Controller → Service → Repository** pattern
- DTOs pour découpler API et entités
- Gestion centralisée des exceptions
- Validation des données (Bean Validation)

### Frontend
- Components découplés
- Services pour logique métier et HTTP
- Models TypeScript typés
- Reactive forms

## 📊 Modèle de Données

### Entité: Application (Postulation)
```
- id: Long (PK)
- company: String (entreprise)
- position: String (poste)
- applicationDate: LocalDate
- currentStatus: Enum (SENT, INTERVIEW, REJECTED, ACCEPTED)
- notes: String (text)
- documentFileName: String (nom fichier uploadé) 🆕
- documentPath: String (chemin stockage) 🆕
- documentContentType: String (MIME type) 🆕
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

### Entité: StatusHistory (Phase 2)
```
- id: Long (PK)
- applicationId: Long (FK)
- oldStatus: Enum
- newStatus: Enum
- changedAt: LocalDateTime
- comment: String (optionnel)
```

## 🔌 API Endpoints (MVP)

```
GET    /api/applications              - Liste toutes les postulations
GET    /api/applications/{id}         - Récupère une postulation
POST   /api/applications              - Crée une postulation
PUT    /api/applications/{id}         - Met à jour une postulation
DELETE /api/applications/{id}         - Supprime une postulation
GET    /api/applications?status=X     - Filtre par statut

# Endpoints Documents 🆕
POST   /api/applications/{id}/document    - Upload document justificatif
GET    /api/applications/{id}/document    - Télécharge le document
DELETE /api/applications/{id}/document    - Supprime le document
```

## 🚀 Déploiement Local

```bash
# Lancer la base de données
docker-compose up -d postgres

# Backend
cd backend
./mvnw spring-boot:run

# Frontend
cd frontend
npm install
ng serve
```

## ✅ Checklist Jour 1

**Backend:**
- [ ] Setup Spring Boot projet
- [ ] Configuration PostgreSQL
- [ ] Entity Application + Enum Status
- [ ] Service layer avec logique métier
- [ ] Controller REST avec tous les endpoints CRUD
- [ ] **Upload de fichiers (MultipartFile)** 🆕
- [ ] **Stockage fichiers sur disque (dossier uploads/)** 🆕
- [ ] **Endpoints download/delete documents** 🆕
- [ ] CORS configuration
- [ ] Tests manuels (Postman)

**Frontend:**
- [ ] Setup Angular projet
- [ ] Installation Angular Material
- [ ] Service HTTP pour API calls
- [ ] Component liste avec tableau Material
- [ ] Component dialog pour formulaire
- [ ] **Input file upload dans formulaire** 🆕
- [ ] **Affichage indicateur document dans liste** 🆕
- [ ] **Bouton télécharger/supprimer document** 🆕
- [ ] Routing basique
- [ ] Filtres par statut

**DevOps:**
- [ ] Docker Compose avec PostgreSQL
- [ ] Volume Docker pour persistance fichiers uploads 🆕
- [ ] README avec instructions
- [ ] Git init + premiers commits
- [ ] Push sur GitHub

## 🎯 Objectifs Business
- Démontrer capacité à créer une app full-stack from scratch
- Montrer architecture propre et maintenable
- Code propre et commenté
- Commits Git réguliers et descriptifs
- Documentation claire

---

**Note**: Ce projet sert de preuve de compétences pour recherche emploi développeur Junior/Medior en Belgique.
