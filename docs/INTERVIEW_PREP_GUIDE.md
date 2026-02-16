# 🎯 GUIDE PRÉPARATION ENTRETIENS - Sprint 10 jours

## 📅 PLANNING

**Jours 1-3** : Projet + Refresh technique en parallèle (2h théorie/jour)
**Jours 4-7** : Préparation intensive entretiens (4-6h/jour)
**Jours 8+** : Postulations massives + entretiens

---

## 🎓 PARTIE 1 : REFRESH TECHNIQUE (Jours 1-3)

### 📘 JAVA - Concepts Essentiels

#### Questions Fréquentes
1. **Différence entre == et .equals() ?**
   - `==` compare les références mémoire
   - `.equals()` compare le contenu des objets
   ```java
   String a = new String("test");
   String b = new String("test");
   a == b        // false (différentes références)
   a.equals(b)   // true (même contenu)
   ```

2. **Qu'est-ce que l'héritage ? Interface vs Classe Abstraite ?**
   - **Interface** : contrat pur, méthodes abstraites (Java 8+: default methods)
   - **Classe abstraite** : peut avoir état et implémentation partielle
   - Java = single inheritance (extends 1 classe) mais multiple interfaces

3. **Collection Framework : List vs Set vs Map ?**
   - **List** : ordre, doublons ok (ArrayList, LinkedList)
   - **Set** : pas de doublons (HashSet, TreeSet)
   - **Map** : clé-valeur (HashMap, TreeMap)

4. **Try-catch-finally et exceptions checked vs unchecked ?**
   - **Checked** : IOException, SQLException (obligé de catch)
   - **Unchecked** : RuntimeException, NullPointerException (optionnel)
   - **Finally** : toujours exécuté (même si exception)

5. **Qu'est-ce que le garbage collector ?**
   - Gestion automatique de la mémoire
   - Supprime objets non référencés
   - On ne contrôle pas directement

#### Mini-exercices
```java
// Exercice 1: Inverser une String
public String reverse(String str) {
    return new StringBuilder(str).reverse().toString();
}

// Exercice 2: Trouver doublons dans array
public List<Integer> findDuplicates(int[] arr) {
    Set<Integer> seen = new HashSet<>();
    Set<Integer> duplicates = new HashSet<>();
    for (int num : arr) {
        if (!seen.add(num)) duplicates.add(num);
    }
    return new ArrayList<>(duplicates);
}

// Exercice 3: Filtrer liste avec Stream API
List<String> filtered = list.stream()
    .filter(s -> s.startsWith("A"))
    .collect(Collectors.toList());
```

---

### 🌐 ANGULAR/TYPESCRIPT - Concepts Essentiels

#### Questions Fréquentes
1. **Component vs Service vs Directive ?**
   - **Component** : UI + logique présentation (@Component)
   - **Service** : logique métier réutilisable (@Injectable)
   - **Directive** : modifie comportement DOM (@Directive)

2. **Reactive Forms vs Template-driven Forms ?**
   - **Reactive** : FormGroup, FormControl, validations code (recommandé)
   - **Template-driven** : ngModel, validations HTML

3. **Observable vs Promise ?**
   - **Promise** : single value, not cancellable
   - **Observable** : stream values, lazy, cancellable, operators RxJS

4. **Lifecycle hooks ?**
   - ngOnInit : initialisation après constructor
   - ngOnDestroy : cleanup avant destruction
   - ngOnChanges : détection changements @Input

5. **Dependency Injection ?**
   - Fournir instances de services aux components
   - `providedIn: 'root'` = singleton app-wide

#### Mini-exercices TypeScript
```typescript
// Exercice 1: Type vs Interface
interface User {
  name: string;
  age: number;
}

type UserRole = 'admin' | 'user' | 'guest';

// Exercice 2: Generic function
function firstElement<T>(arr: T[]): T | undefined {
  return arr[0];
}

// Exercice 3: Observable + pipe
this.http.get<User[]>('/api/users').pipe(
  map(users => users.filter(u => u.age > 18)),
  catchError(err => of([]))
).subscribe(adults => console.log(adults));
```

---

### 🗄️ SQL - Concepts Essentiels

#### Questions Fréquentes
1. **JOIN types ?**
   - **INNER JOIN** : seulement correspondances
   - **LEFT JOIN** : tout à gauche + correspondances droite
   - **RIGHT JOIN** : tout à droite + correspondances gauche
   - **FULL JOIN** : tout des deux côtés

2. **Primary Key vs Foreign Key ?**
   - **PK** : identifiant unique table
   - **FK** : référence PK autre table (relation)

3. **Index : c'est quoi et pourquoi ?**
   - Structure de données pour accélérer recherches
   - Trade-off : plus rapide SELECT, plus lent INSERT/UPDATE

4. **Transactions et ACID ?**
   - **A**tomicity : tout ou rien
   - **C**onsistency : état valide
   - **I**solation : transactions indépendantes
   - **D**urability : persistance données

#### Mini-exercices SQL
```sql
-- Exercice 1: Compter postulations par statut
SELECT current_status, COUNT(*) as total
FROM applications
GROUP BY current_status;

-- Exercice 2: Top 5 entreprises avec le plus de postulations
SELECT company, COUNT(*) as count
FROM applications
GROUP BY company
ORDER BY count DESC
LIMIT 5;

-- Exercice 3: Postulations avec entretien obtenu
SELECT company, position, application_date
FROM applications
WHERE current_status IN ('INTERVIEW', 'ACCEPTED')
ORDER BY application_date DESC;
```

---

### 🌱 SPRING BOOT - Concepts Essentiels

#### Questions Fréquentes
1. **@RestController vs @Controller ?**
   - **@RestController** = @Controller + @ResponseBody (JSON auto)
   - **@Controller** : retourne vues (Thymeleaf)

2. **@Autowired et injection de dépendances ?**
   - Spring gère création et injection objets
   - Préférer constructor injection

3. **JPA/Hibernate : qu'est-ce que c'est ?**
   - **JPA** : spécification Java pour ORM
   - **Hibernate** : implémentation de JPA
   - Mapping objet ↔ table

4. **@Entity, @Id, @GeneratedValue ?**
   - **@Entity** : classe = table BDD
   - **@Id** : primary key
   - **@GeneratedValue** : auto-increment

---

### 🔧 GIT - Commandes Essentielles

```bash
# Workflow de base
git init                    # Initialiser repo
git add .                   # Stager tous changements
git commit -m "message"     # Commit avec message
git push origin main        # Push vers remote

# Branches
git checkout -b feature     # Créer et switcher branch
git merge feature           # Merger branch dans current

# Collaboratif
git pull                    # Fetch + merge
git stash                   # Sauver changements temporairement
git stash pop               # Restaurer changements

# Historique
git log                     # Voir commits
git diff                    # Voir changements
git status                  # État working directory
```

---

## 🎤 PARTIE 2 : QUESTIONS COMPORTEMENTALES (Jours 4-7)

### 🚨 TON TROU DANS LE CV - PRÉPARER LA RÉPONSE

**La question viendra : "Que faisiez-vous ces 2-3 dernières années ?"**

#### ✅ VERSION RECOMMANDÉE (honnête + positive)
> "Après mes 18 mois en tant que développeur, j'ai pris du temps pour explorer l'entrepreneuriat dans la crypto et le trading. J'ai également continué à développer en freelance sur des projets personnels. Cette période m'a appris énormément sur l'autonomie et la gestion de projet. Maintenant, je suis motivé à revenir dans le développement professionnel avec une équipe, et j'ai d'ailleurs récemment créé [montrer projet GitHub] pour me remettre à niveau sur les technologies actuelles."

**Points clés :**
- ✅ Honnête mais cadré positivement
- ✅ Montre initiative (entrepreneuriat, projets)
- ✅ Termine sur l'action présente (projet récent)
- ✅ Montre motivation pour retour pro

#### ❌ À ÉVITER
- ❌ "J'ai rien fait" ou "J'étais perdu"
- ❌ Trop de détails sur pourquoi crypto/trading
- ❌ Excuses ou ton défensif
- ❌ Mentir (vérifiable sur LinkedIn)

---

### 💼 QUESTIONS COMPORTEMENTALES CLASSIQUES

#### 1. "Parlez-moi de vous"
**Structure PRÉSENT → PASSÉ → FUTUR (2 min max)**

> "Je suis développeur full-stack avec une formation en informatique de gestion (Bac+3) et 18 mois d'expérience professionnelle en Java/Angular. Après cette première expérience, j'ai pris du temps pour des projets entrepreneuriaux, et maintenant je reviens avec motivation dans le développement d'applications. J'ai récemment créé un tracker de postulations en full-stack pour me remettre à niveau, et je cherche maintenant une position où je peux contribuer à une équipe sur des projets concrets."

#### 2. "Pourquoi cette entreprise ?"
**Recherche AVANT l'entretien :**
- Site web : secteur, produits, valeurs
- LinkedIn : taille, croissance, équipe tech
- Glassdoor : culture d'entreprise

**Template réponse :**
> "J'ai vu que vous travaillez sur [produit/secteur], ce qui m'intéresse particulièrement parce que [raison]. Votre stack technique [technos mentionnées] correspond bien à mes compétences, et j'apprécie [valeur de l'entreprise trouvée]."

#### 3. "Quelles sont vos forces et faiblesses ?"

**FORCES** (avec exemples) :
- Apprentissage rapide : "Je me suis remis sur Angular 17 en 2 jours"
- Autonomie : "J'ai créé un projet full-stack seul de A à Z"
- Problem-solving : "J'aime débugger et comprendre en profondeur"

**FAIBLESSES** (honnête + plan d'action) :
- ✅ "Je manque de pratique sur [techno spécifique], mais je suis en train de [action concrète]"
- ✅ "J'ai tendance à être perfectionniste, je travaille sur savoir prioriser MVP vs features"
- ❌ "Je suis pas bon en [truc critique pour le job]"

#### 4. "Décrivez un conflit/problème technique difficile"
**Structure STAR :**
- **S**ituation : contexte
- **T**ask : ton rôle/objectif
- **A**ction : ce que tu as fait
- **R**ésultat : outcome

**Exemple :**
> "Sur un projet précédent (S), j'ai eu un bug de performance sur une requête SQL qui prenait 10s (T). J'ai analysé l'execution plan, ajouté des index sur les colonnes filtrées, et optimisé la requête en évitant les sous-requêtes (A). Le temps est passé à 0.5s et le client était satisfait (R)."

#### 5. "Où vous voyez-vous dans 3-5 ans ?"
**Éviter :** "CTO" ou "ma propre boîte" (red flag pour employeur)

**Bien :**
> "Je veux continuer à progresser techniquement, contribuer à des projets de plus en plus complexes, et potentiellement évoluer vers un rôle senior où je peux aussi mentorer des juniors."

---

### 🔥 QUESTIONS PIÈGES

#### "Pourquoi devrions-nous vous embaucher ?"
**Ne PAS dire :** "J'ai besoin d'un job" ou "Je suis le meilleur"

**DIRE :**
> "Je combine formation solide (Bac+3), expérience pratique (18 mois), et motivation réelle pour revenir dans le dev. Mon projet récent montre que je suis opérationnel sur votre stack, et je suis prêt à m'investir à long terme."

#### "Quelles sont vos prétentions salariales ?"
**Recherche AVANT :**
- Glassdoor / Payscale pour Belgique
- Junior dev : 2500-3200€ brut/mois
- Medior : 3200-4000€ brut/mois

**Réponse :**
> "D'après mes recherches, la fourchette pour ce type de poste en Belgique est [X-Y]€. Je suis ouvert à discuter en fonction des responsabilités et avantages."

---

## 🧠 PARTIE 3 : QUESTIONS TECHNIQUES LIVE CODING (Jours 5-6)

### Algorithmes Basiques à Maîtriser

#### 1. FizzBuzz (classique)
```java
for (int i = 1; i <= 100; i++) {
    if (i % 15 == 0) System.out.println("FizzBuzz");
    else if (i % 3 == 0) System.out.println("Fizz");
    else if (i % 5 == 0) System.out.println("Buzz");
    else System.out.println(i);
}
```

#### 2. Palindrome
```java
public boolean isPalindrome(String str) {
    String cleaned = str.toLowerCase().replaceAll("[^a-z0-9]", "");
    return cleaned.equals(new StringBuilder(cleaned).reverse().toString());
}
```

#### 3. Trouver nombre manquant dans array 1-n
```java
public int findMissing(int[] arr, int n) {
    int expectedSum = n * (n + 1) / 2;
    int actualSum = 0;
    for (int num : arr) actualSum += num;
    return expectedSum - actualSum;
}
```

#### 4. Compter occurrences caractères
```java
public Map<Character, Integer> countChars(String str) {
    Map<Character, Integer> map = new HashMap<>();
    for (char c : str.toCharArray()) {
        map.put(c, map.getOrDefault(c, 0) + 1);
    }
    return map;
}
```

---

## ✅ CHECKLIST PRÉPARATION ENTRETIEN

### Avant l'entretien (J-1)
- [ ] Recherche sur l'entreprise (site, LinkedIn, Glassdoor)
- [ ] Revoir job description et identifier keywords techniques
- [ ] Préparer questions à poser (2-3 questions)
- [ ] Tester matériel (micro, caméra si visio)
- [ ] Relire ton CV et projet GitHub

### Questions à TOUJOURS poser à la fin
1. "Comment est organisée l'équipe de développement ?"
2. "Quelles sont les technologies utilisées au quotidien ?"
3. "Quelles sont les prochaines étapes du processus de recrutement ?"
4. (Si ESN) "Pouvez-vous me parler du/des clients sur lesquels je travaillerais ?"

### Pendant l'entretien
- [ ] Écouter attentivement avant de répondre
- [ ] Penser à voix haute en live coding
- [ ] Demander clarifications si besoin
- [ ] Rester positif et enthousiaste
- [ ] Prendre notes des questions techniques

### Après l'entretien
- [ ] Email de remerciement dans les 24h
- [ ] Noter feedback pour améliorer prochains entretiens
- [ ] Follow-up si pas de réponse après 1 semaine

---

## 📝 SIMULATION ENTRETIEN - EXERCICE PRATIQUE

**Entraîne-toi à répondre à VOIX HAUTE (chronomètre) :**

1. Présente-toi en 2 minutes
2. Explique ton trou de CV
3. Décris ton projet de tracking
4. Code FizzBuzz en live
5. Explique la différence entre List et Set

**Timing idéal :**
- Présentation : 2 min
- Question comportementale : 1-2 min
- Question technique : 2-3 min
- Live coding : 5-10 min

---

## 🎯 RESSOURCES RAPIDES

### Pour réviser vite
- **Java** : Oracle Java Tutorials (docs.oracle.com)
- **Angular** : Angular.io official docs
- **SQL** : SQLZoo.net (exercices interactifs)
- **Algo** : LeetCode Easy problems (10-15 problèmes suffisent)

### Mock interviews
- **Pramp** : peer-to-peer mock interviews (gratuit)
- **Interviewing.io** : mock interviews avec engineers

---

## 🚀 STRATÉGIE POSTULATION (Jour 8+)

### Volume = Success
- **Target** : 10-20 postulations/jour
- **Plateformes** : LinkedIn, Indeed, StepStone, Glassdoor

### Types d'offres à cibler
1. **Junior Developer** (ton niveau actuel)
2. **Medior Developer** (si 3+ ans expérience demandés, tente quand même)
3. **ESN/Consulting** (recrutent souvent, bon pour restart)

### CV Optimization
- **Titre** : "Développeur Full-Stack Java/Angular"
- **Projets** : Mettre en avant ton tracker (lien GitHub)
- **Skills** : Java, Spring Boot, Angular, TypeScript, PostgreSQL, Git
- **Expérience** : Focus sur réalisations concrètes

### Cover Letter Template (court)
> Bonjour,
>
> Développeur full-stack avec formation Bac+3 et expérience en Java/Angular, je candidate pour le poste de [POSTE]. Après une pause pour projets entrepreneuriaux, je suis motivé à rejoindre une équipe de développement. J'ai récemment créé un tracker d'applications full-stack (GitHub: [lien]) pour me remettre à niveau sur les technologies actuelles.
>
> Je serais ravi d'échanger sur comment contribuer à vos projets.
>
> Cordialement,
> [NOM]

---

## 🎓 RÉSUMÉ - PLAN 10 JOURS

**J1-J3** : Projet tracker (70%) + Refresh technique (30%)
**J4-J5** : Questions comportementales + simulation
**J6-J7** : Live coding practice + questions techniques
**J8-J10** : Postulations massives (10-20/jour)
**J11+** : Entretiens

**TU ES PRÊT ! 💪**
