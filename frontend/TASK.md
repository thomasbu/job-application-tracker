\# TASK: Tri + Recherche + Pagination



\## Objectif

Ajouter tri, recherche et pagination au tableau des applications dans `application-list`.



\## Contexte technique

\- \*\*Angular 21\*\* avec standalone components

\- \*\*Signals\*\* pour state management (`signal()`, `computed()`)

\- \*\*Syntaxe moderne\*\* : `@if`, `@for`, `@switch`

\- \*\*Pas de Angular Material\*\* - Design custom cohérent avec styles existants

\- \*\*Fichiers sans suffixes\*\* : `.ts` (pas `.component.ts`)



\## État actuel

Fichier principal : `src/app/components/application-list/`

\- `application-list.ts` - Component avec Signal applications()

\- `application-list.html` - Template avec tableau

\- `application-list.scss` - Styles



Le component affiche déjà :

\- Stats dashboard (compteurs)

\- Filtre par statut

\- Tableau avec colonnes : Company, Position, Applied Date, Status, Documents, Actions



\## Features à implémenter



\### 1. TRI DU TABLEAU

\*\*Comportement :\*\*

\- Colonnes triables : Company, Position, Applied Date, Status

\- Clic sur header → tri ascendant

\- Re-clic → tri descendant

\- Re-clic → retour à l'ordre original

\- Indicateur visuel : flèche ↑ (asc) / ↓ (desc) à côté du nom de colonne



\*\*Implémentation :\*\*

\- Ajouter Signal `sortColumn` et `sortDirection`

\- Computed signal pour `sortedApplications()`

\- Fonction `onSort(column)` appelée au clic sur header

\- Icône SVG pour les flèches



\### 2. RECHERCHE

\*\*Comportement :\*\*

\- Barre de recherche au-dessus du tableau

\- Recherche dans : `company` et `position` (case-insensitive)

\- Temps réel (pas de bouton submit)

\- Bouton "X" pour clear la recherche



\*\*Implémentation :\*\*

\- Signal `searchTerm`

\- Computed signal pour `filteredApplications()` (après tri)

\- Input avec `(input)` event

\- Bouton clear avec `(click)`



\### 3. PAGINATION

\*\*Comportement :\*\*

\- 10 items par page par défaut

\- Sélecteur : 10 / 25 / 50 items par page

\- Boutons Previous / Next

\- Affichage : "Page 1 of 3"

\- Désactiver Previous si page 1

\- Désactiver Next si dernière page



\*\*Implémentation :\*\*

\- Signals : `currentPage`, `itemsPerPage`

\- Computed : `paginatedApplications()` (après tri + recherche)

\- Computed : `totalPages()`

\- Fonctions : `nextPage()`, `previousPage()`, `onPageSizeChange()`



\## Pipeline de données (ordre important)

```

applications() 

&nbsp; → sortedApplications() (tri)

&nbsp; → filteredApplications() (recherche)

&nbsp; → paginatedApplications() (pagination)

&nbsp; → affichage

```



\## Design attendu



\### Barre de recherche

```

┌────────────────────────────────────────┐

│ 🔍 Search applications...         \[X] │

└────────────────────────────────────────┘

```



\### Headers avec tri

```

COMPANY ↓ | POSITION | APPLIED DATE ↑ | STATUS

```



\### Pagination

```

\[← Previous]  Page 2 of 5  \[10 ▼]  \[Next →]

```



\## Contraintes

\- \*\*Ne pas casser\*\* : Stats dashboard, filtre par statut existant

\- \*\*Responsive\*\* : Adapter sur mobile

\- \*\*Performance\*\* : Utiliser computed() pour éviter recalculs inutiles

\- \*\*Style cohérent\*\* : Couleurs, spacing, border-radius identiques au reste

\- \*\*Accessibilité\*\* : Labels, aria-labels sur boutons



\## Fichiers à modifier

1\. `src/app/components/application-list/application-list.ts`

2\. `src/app/components/application-list/application-list.html`

3\. `src/app/components/application-list/application-list.scss`



\## Styles à utiliser (déjà définis dans styles.scss)

\- Colors : `var(--primary)`, `var(--gray-600)`, etc.

\- Spacing : `var(--spacing-md)`, `var(--spacing-lg)`

\- Border radius : `var(--radius-md)`

\- Buttons : `.btn`, `.btn-secondary`, `.btn-ghost`

\- Inputs : `.input`



\## Tests à effectuer

\- \[ ] Tri fonctionne sur chaque colonne

\- \[ ] Recherche filtre correctement

\- \[ ] Pagination affiche le bon nombre d'items

\- \[ ] Previous/Next naviguent correctement

\- \[ ] Changer items per page reset à page 1

\- \[ ] Stats dashboard reste fonctionnel

\- \[ ] Filtre par statut reste fonctionnel

\- \[ ] Responsive sur mobile



\## Important

Garde le code existant intact. Ajoute les nouvelles features sans modifier la logique actuelle des stats et du filtre par statut.

