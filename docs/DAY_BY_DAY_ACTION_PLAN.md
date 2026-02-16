# 📅 PLAN D'ACTION JOUR PAR JOUR - Sprint 10 jours

## 🎯 OBJECTIF GLOBAL
Avoir un projet GitHub déployable + être prêt pour entretiens techniques et comportementaux

---

## JOUR 1 - JEUDI : FONDATIONS PROJET + REFRESH JAVA

### Matin (4h) : Setup Backend
- [ ] **8h-9h** : Créer repo GitHub "job-application-tracker"
- [ ] **9h-10h30** : Setup Spring Boot projet
  - Initialiser avec Spring Initializr
  - Configurer PostgreSQL
  - Créer Entity Application + Enum Status + **champs documents** 🆕
- [ ] **10h30-12h** : Repository + Service layer
  - ApplicationRepository avec queries
  - ApplicationService interface + implémentation
  - **FileStorageService pour upload/download** 🆕
  - DTOs (ApplicationDTO avec hasDocument, CreateRequest, UpdateRequest)

### Après-midi (4h) : Controller + Tests
- [ ] **13h-15h** : Controller REST
  - Tous les endpoints CRUD
  - **Endpoints upload/download/delete document** 🆕
  - Validation avec @Valid
  - Exception handling global
- [ ] **15h-17h** : Tests manuels
  - Docker Compose PostgreSQL
  - Tester avec Postman tous les endpoints CRUD
  - **Tester upload fichier PDF/image** 🆕
  - **Tester download et vérifier fichier** 🆕
  - Corriger bugs éventuels

### Soir (2h) : Refresh Théorique Java
- [ ] **19h-20h** : Revoir concepts clés
  - Collections (List, Set, Map)
  - Exceptions (checked vs unchecked)
  - Héritage vs Interface
- [ ] **20h-21h** : Mini-exercices
  - Coder 3 algos basiques (voir INTERVIEW_PREP_GUIDE.md)
  - FizzBuzz, Palindrome, Array manipulation

**Livrables fin J1 :**
- ✅ Backend complet et fonctionnel
- ✅ **Système upload/download documents opérationnel** 🆕
- ✅ Tests Postman validés (CRUD + documents)
- ✅ Refresh Java concepts basiques

---

## JOUR 2 - VENDREDI : FRONTEND ANGULAR

### Matin (4h) : Setup Frontend
- [ ] **8h-9h** : Créer projet Angular
  - ng new job-tracker-frontend
  - Installer Angular Material
  - Configuration routing
- [ ] **9h-11h** : Models + Service
  - Créer models TypeScript (Application, ApplicationStatus)
  - ApplicationService avec HttpClient
  - Environnement config (API URL)
- [ ] **11h-12h** : Component liste (structure)
  - Créer ApplicationListComponent
  - Material Table basique
  - Fetch data du backend

### Après-midi (4h) : Formulaires + Actions
- [ ] **13h-15h** : Dialog formulaire
  - ApplicationDialogComponent avec Reactive Form
  - Validations
  - **Input file upload (accept PDF/images/Word)** 🆕
  - **Preview nom fichier sélectionné** 🆕
  - Mode create vs edit
- [ ] **15h-17h** : Actions CRUD + Documents
  - Bouton "Nouvelle postulation"
  - Éditer (ouvrir dialog)
  - **Upload document après création postulation** 🆕
  - **Download document (bouton dans liste)** 🆕
  - **Delete document avec confirmation** 🆕
  - Supprimer postulation (confirmation + snackbar)
  - Filtres par statut (dropdown)

### Soir (2h) : Refresh Angular/TypeScript
- [ ] **19h-20h** : Revoir concepts
  - Component lifecycle
  - Observable vs Promise
  - Reactive Forms
- [ ] **20h-21h** : TypeScript types/interfaces
  - Exercices génériques
  - Type narrowing

**Livrables fin J2 :**
- ✅ Frontend complet et connecté au backend
- ✅ CRUD fonctionnel dans UI
- ✅ **Upload/Download/Delete documents dans UI** 🆕
- ✅ **Indicateur visuel documents dans liste** 🆕
- ✅ App utilisable en local avec documents

---

## JOUR 3 - SAMEDI : POLISH + HISTORIQUE (Phase 2 début)

### Matin (3h) : Finitions MVP
- [ ] **9h-10h** : UI/UX improvements
  - Loading spinners
  - Empty states
  - Error messages clairs
- [ ] **10h-11h** : Tri et compteurs
  - Tri par date dans tableau
  - Compteur postulations par statut (header)
- [ ] **11h-12h** : Docker + README
  - docker-compose.yml complet (backend + frontend + db)
  - **Volume pour persistance fichiers uploads** 🆕
  - README avec instructions setup

### Après-midi (3h) : Historique Statut (Phase 2)
- [ ] **13h-15h** : Backend historique
  - Entity StatusHistory
  - Relation OneToMany avec Application
  - Service pour enregistrer changements
  - Endpoint GET /api/applications/{id}/history
- [ ] **15h-16h** : Tests historique backend

### Soir (2h) : Git + Deploy GitHub
- [ ] **18h-19h** : Cleanup code
  - Commenter code
  - Supprimer console.logs
  - Formater
- [ ] **19h-20h** : Git push
  - Commits descriptifs
  - Push to GitHub
  - Vérifier README affichage

**Livrables fin J3 :**
- ✅ Projet MVP complet + début Phase 2
- ✅ GitHub repo public et propre
- ✅ README instructions claires

---

## JOUR 4 - DIMANCHE : PREP ENTRETIENS COMPORTEMENTAUX

### Matin (3h) : Préparer réponses
- [ ] **9h-10h** : "Parlez-moi de vous"
  - Écrire script 2 min
  - Pratiquer à voix haute 5x
  - Chronomètre
- [ ] **10h-11h** : Explication trou CV
  - Préparer 2 versions (courte 30s, longue 2min)
  - Pratiquer ton et assurance
- [ ] **11h-12h** : Forces/Faiblesses
  - Lister 3 forces avec exemples concrets
  - Choisir 1-2 faiblesses + plan action

### Après-midi (3h) : Questions classiques
- [ ] **13h-14h** : "Pourquoi cette entreprise ?"
  - Template réponse
  - Recherche 3 entreprises cibles et adapter
- [ ] **14h-15h** : "Conflit/Problème technique"
  - Préparer 2 histoires STAR
  - Détailler actions concrètes
- [ ] **15h-16h** : Questions à poser
  - Lister 5 questions pertinentes
  - Adapter selon type entreprise (startup, ESN, corporate)

### Soir (2h) : Simulation complète
- [ ] **18h-19h** : Mock interview comportemental
  - Enregistre-toi (vidéo)
  - Réponds aux 6 questions classiques
- [ ] **19h-20h** : Revoir vidéo
  - Identifier tics de langage
  - Améliorer posture/ton
  - Refaire questions faibles

**Livrables fin J4 :**
- ✅ Réponses scriptées et pratiquées
- ✅ Confiance sur questions comportementales
- ✅ Vidéo simulation pour référence

---

## JOUR 5 - LUNDI : PREP TECHNIQUE JAVA/SPRING

### Matin (3h) : Révision concepts
- [ ] **9h-10h** : Java core
  - Collections framework
  - Streams API
  - Exception handling
  - Faire quizz en ligne (20 questions)
- [ ] **10h-11h** : Spring Boot
  - Annotations (@RestController, @Service, @Autowired)
  - Injection de dépendances
  - JPA/Hibernate basics
- [ ] **11h-12h** : REST API
  - HTTP methods (GET, POST, PUT, DELETE)
  - Status codes (200, 201, 404, 500)
  - CORS, validation

### Après-midi (3h) : Questions techniques probables
- [ ] **13h-14h** : Préparer explications projet
  - Architecture de ton tracker
  - Choix technologiques
  - Difficultés rencontrées
- [ ] **14h-15h** : Questions pièges
  - "Différence == vs equals ?" (avec ton exemple)
  - "Qu'est-ce qu'une transaction ?"
  - "Comment gérer exception API ?"
- [ ] **15h-16h** : Whiteboard coding
  - Expliquer diagramme de classes
  - Dessiner architecture 3-tiers

### Soir (2h) : SQL refresh
- [ ] **18h-19h** : Requêtes basiques
  - SELECT, WHERE, JOIN
  - GROUP BY, ORDER BY
  - Aggregate functions
- [ ] **19h-20h** : Exercices SQL
  - 5 requêtes sur ton schéma applications
  - Optimisation avec indexes

**Livrables fin J5 :**
- ✅ Maîtrise concepts Java/Spring pour expliquer projet
- ✅ Réponses techniques fluides
- ✅ SQL opérationnel

---

## JOUR 6 - MARDI : LIVE CODING PRACTICE

### Matin (3h) : Algorithmes classiques
- [ ] **9h-10h** : Arrays & Strings
  - Reverse string
  - Find duplicates
  - Two sum problem
- [ ] **10h-11h** : Loops & Conditions
  - FizzBuzz (plusieurs variantes)
  - Fibonacci sequence
  - Prime numbers
- [ ] **11h-12h** : Collections
  - Count characters occurrences
  - Group anagrams
  - Merge sorted lists

### Après-midi (3h) : LeetCode Easy
- [ ] **13h-16h** : Résoudre 10 problèmes Easy
  - Arrays (5 problèmes)
  - Strings (3 problèmes)
  - Hash Maps (2 problèmes)
  - Timer : 20-30 min par problème max

### Soir (2h) : Simulation live coding
- [ ] **18h-19h** : Mock technical interview
  - Choisir 3 problèmes non faits
  - Résoudre en expliquant à voix haute
  - Chronomètre 15 min par problème
- [ ] **19h-20h** : Analyser performance
  - Identifier patterns
  - Revoir solutions optimales

**Livrables fin J6 :**
- ✅ 15+ algos résolus
- ✅ Confort avec live coding
- ✅ Méthodologie problem-solving claire

---

## JOUR 7 - MERCREDI : ANGULAR/TS + SIMULATION GLOBALE

### Matin (3h) : Angular deep dive
- [ ] **9h-10h** : Components & Services
  - Lifecycle hooks détaillés
  - Communication parent-child
  - Dependency injection
- [ ] **10h-11h** : RxJS & Observables
  - Operators (map, filter, switchMap)
  - Error handling
  - Unsubscribe patterns
- [ ] **11h-12h** : Forms & Validation
  - Reactive forms en détail
  - Custom validators
  - Async validators

### Après-midi (3h) : TypeScript avancé
- [ ] **13h-14h** : Types & Interfaces
  - Generics
  - Union types
  - Type guards
- [ ] **14h-15h** : Exercices pratiques
  - 5 problèmes TypeScript
  - Type-safe API calls
- [ ] **15h-16h** : Refactor code projet
  - Ajouter types stricts
  - Améliorer type safety

### Soir (2h) : Simulation entretien complète
- [ ] **18h-20h** : Full mock interview (120 min)
  - 10 min : présentation
  - 30 min : questions comportementales
  - 40 min : questions techniques projet
  - 30 min : live coding (2 algos)
  - 10 min : tes questions
  - Enregistrer et revoir

**Livrables fin J7 :**
- ✅ Maîtrise Angular/TypeScript
- ✅ Simulation complète réussie
- ✅ Confiance maximale

---

## JOUR 8 - JEUDI : CV + POSTULATIONS MASSIVES

### Matin (3h) : Optimisation CV
- [ ] **9h-10h** : Refonte CV
  - Titre clair : "Développeur Full-Stack Java/Angular"
  - Section Projets avec lien GitHub
  - Skills en bullet points
  - Expérience avec réalisations quantifiées
- [ ] **10h-11h** : LinkedIn update
  - Photo professionnelle
  - Headline optimisée
  - About section avec keywords
  - Ajouter projet dans "Projets"
- [ ] **11h-12h** : Portfolio GitHub
  - README projet impeccable
  - Screenshots dans README
  - Commits propres et descriptifs

### Après-midi (4h) : POSTULATIONS
- [ ] **13h-17h** : Postuler 20 offres minimum
  - LinkedIn Jobs (10 offres)
  - Indeed Belgique (5 offres)
  - StepStone (5 offres)
  - **Priorité** : Junior/Medior Developer Java/Angular
  - **Mots-clés** : Java, Spring, Angular, Full-Stack, Belgique

### Soir (2h) : Tracking + Prep
- [ ] **18h-19h** : Utiliser TON tracker pour suivre postulations
  - Ajouter les 20 offres
  - Notes sur chaque entreprise
- [ ] **19h-20h** : Préparer 5 cover letters personnalisées
  - Pour top 5 entreprises cibles

**Livrables fin J8 :**
- ✅ CV/LinkedIn optimisés
- ✅ 20 candidatures envoyées
- ✅ Tracking actif

---

## JOUR 9 - VENDREDI : POSTULATIONS + PREP CIBLÉE

### Matin (2h) : Recherche entreprises
- [ ] **9h-10h** : Identifier 30 nouvelles cibles
  - ESN Belgique (10)
  - Startups tech Bruxelles (10)
  - Corporates avec équipes dev (10)
- [ ] **10h-11h** : Recherche détaillée top 10
  - Site web, stack tech
  - LinkedIn team dev
  - Glassdoor reviews

### Après-midi (4h) : POSTULATIONS
- [ ] **13h-17h** : Postuler 20 nouvelles offres
  - Adapter cover letter si nécessaire
  - Mentionner projet GitHub dans message
  - Follow-up sur postulations J8 (si pas de réponse auto)

### Soir (2h) : Network + Prep
- [ ] **18h-19h** : LinkedIn networking
  - Contacter 10 recruteurs tech Belgique
  - Message court et pro
- [ ] **19h-20h** : Préparer réponses spécifiques
  - Revoir job descriptions postulées
  - Adapter tes réponses aux keywords

**Livrables fin J9 :**
- ✅ 40 candidatures totales
- ✅ Network activé
- ✅ Réponses adaptées prêtes

---

## JOUR 10 - SAMEDI : AMÉLIORATION CONTINUE + ENTRETIENS

### Matin (3h) : Polish projet Phase 2
- [ ] **9h-12h** : Finir historique statut frontend
  - Component vue détail
  - Timeline Material
  - Affichage historique complet
  - Push to GitHub

### Après-midi (3h) : Préparation continue
- [ ] **13h-14h** : Revoir notes entretiens
  - Points faibles identifiés
  - Re-pratiquer
- [ ] **14h-15h** : Veille techno rapide
  - Lire 3 articles récents Java/Angular
  - Être au courant tendances
- [ ] **15h-16h** : Détente / confiance
  - Relire réussites (projet terminé, X postulations)
  - Mental prep

### Soir : Repos
- **18h+** : Détente, pas de travail
  - Film, sport, hobbies
  - Recharge pour la semaine d'entretiens

**Livrables fin J10 :**
- ✅ Projet Phase 2 complet
- ✅ 40+ candidatures
- ✅ Prêt pour entretiens semaine prochaine

---

## JOURS 11+ : ENTRETIENS & SUIVI

### Process continu
- **Chaque jour** :
  - [ ] Postuler 10 nouvelles offres
  - [ ] Follow-up candidatures >1 semaine sans réponse
  - [ ] Préparer entretiens du jour (recherche entreprise)

- **Après chaque entretien** :
  - [ ] Email remerciement <24h
  - [ ] Noter feedback et points d'amélioration
  - [ ] Mettre à jour statut dans tracker

- **Amélioration projet** :
  - [ ] Continuer enrichissement (stats, auth, tests)
  - [ ] Montrer évolution continue sur GitHub

---

## 🎯 MÉTRIQUES DE SUCCÈS

### Quantitatif
- ✅ 1 projet full-stack GitHub opérationnel
- ✅ 50+ candidatures en 2 semaines
- ✅ 5-10 premiers contacts/entretiens
- ✅ 1-2 offres dans les 3-4 semaines

### Qualitatif
- ✅ Confiance restaurée en compétences dev
- ✅ Discours clair sur trou CV
- ✅ Maîtrise concepts tech pour entretiens
- ✅ Network réactivé

---

## 💪 MOTIVATION

**Tu as tout ce qu'il faut :**
- Formation solide (Bac+3)
- Expérience réelle (18 mois)
- Compétences techniques
- Projet concret récent
- Plan d'action structuré

**Le trou de 2-3 ans n'est PAS un blocage si :**
- Tu l'expliques positivement
- Tu montres action présente (projet)
- Tu démontres motivation et compétence

**CHAQUE JOUR COMPTE. LET'S GO ! 🚀**
