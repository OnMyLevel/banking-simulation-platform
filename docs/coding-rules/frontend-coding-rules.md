# Frontend Coding Rules

# Banking Simulation Platform

## 1. Objectif

Ce document définit les règles de développement frontend pour les applications web du projet.

Applications concernées :

```text
client-web-react
business-web-vue
advisor-admin-angular
```

Objectifs : cohérence, maintenabilité, performance, accessibilité, testabilité et lisibilité.

Références principales :

- GitLab Frontend Development Guidelines
- Juntos Somos Mais Front-end Guideline
- DIT Frontend Coding Standards

---

## 2. Principes frontend

Les frontends doivent respecter trois valeurs principales :

```text
Stability
Speed
Maintainability
```

Règles :

- chaque fonctionnalité doit être stable et testée ;
- l'interface doit rester rapide ;
- les composants doivent être simples à comprendre ;
- la complexité doit être encapsulée ;
- les composants doivent être faiblement couplés.

---

## 3. Architecture frontend globale

Structure recommandée :

```text
src/
├── app/
├── pages/
├── features/
├── components/
├── services/
├── hooks/
├── composables/
├── stores/
├── router/
├── types/
├── utils/
├── constants/
├── assets/
└── styles/
```

Règles :

- `components/` contient les composants réutilisables ;
- `pages/` contient les pages routées ;
- `features/` contient les blocs fonctionnels métier ;
- `services/` contient les appels API ;
- `stores/` contient la gestion d'état ;
- `utils/` contient des fonctions pures et génériques.

---

## 4. React

Application : `frontend/client-web-react`

Règles :

- utiliser TypeScript ;
- utiliser des composants fonctionnels ;
- éviter les composants trop gros ;
- extraire la logique métier dans des hooks ;
- ne pas appeler directement `fetch` dans les composants ;
- utiliser React Query pour les appels API ;
- utiliser les props typées.

---

## 5. Vue

Application : `frontend/business-web-vue`

Règles :

- utiliser Vue 3 ;
- utiliser TypeScript ;
- utiliser Composition API ;
- utiliser Pinia ;
- éviter Vuex ;
- limiter la logique dans les templates ;
- extraire la logique réutilisable dans des composables.

---

## 6. Angular

Application : `frontend/advisor-admin-angular`

Structure recommandée :

```text
src/app/
├── core/
├── shared/
├── features/
├── layout/
└── app.routes.ts
```

Règles :

- utiliser TypeScript strict ;
- utiliser Angular Material ;
- isoler les fonctionnalités dans `features/` ;
- utiliser `core/` pour les services singleton ;
- utiliser `shared/` pour les composants réutilisables ;
- utiliser RxJS proprement ;
- utiliser NgRx seulement si nécessaire.

---

## 7. Nommage des fichiers

Bon :

```text
UserProfile/UserProfile.tsx
UserProfile/UserProfile.vue
UserProfile/UserProfile.component.ts
UserProfile/UserProfile.module.scss
UserProfile/UserProfile.spec.ts
```

Mauvais :

```text
UserProfile/index.tsx
UserProfile/component.tsx
UserProfile/style.scss
component.vue
```

Règles :

- composants en PascalCase ;
- hooks et composables en `useXxx` ;
- services en `XxxService` ;
- types en PascalCase ;
- éviter les fichiers `index` sauf justification.

---

## 8. Imports

Règles :

- imports relatifs dans un même module ;
- alias pour les imports transverses ;
- éviter les imports circulaires ;
- éviter les barrel files abusifs ;
- préférer les imports directs.

Exemple :

```ts
import { AccountCard } from '@/components/AccountCard/AccountCard';
import { useAccounts } from '@/features/accounts/hooks/useAccounts';
```

---

## 9. TypeScript

Règles :

- activer le mode strict ;
- éviter `any` ;
- préférer `unknown` si le type est réellement inconnu ;
- typer les props ;
- typer les réponses API ;
- créer des types métier par domaine.

---

## 10. Appels API

Les composants ne doivent jamais appeler directement `fetch` ou `axios`.

Flux recommandé :

```text
component -> hook/composable/store -> service API -> HTTP client
```

Règles :

- centraliser le client HTTP ;
- gérer l'authentification dans une couche dédiée ;
- propager le correlationId si disponible ;
- typer les réponses ;
- séparer logique API et logique UI.

---

## 11. Erreurs UI

Chaque application doit gérer :

- validation ;
- authentification ;
- autorisation ;
- erreur métier ;
- erreur technique ;
- indisponibilité réseau.

Règles :

- afficher un message compréhensible ;
- prévoir un fallback UI ;
- ne pas bloquer toute l'application pour une erreur locale.

---

## 12. CSS et styling

Règles :

- utiliser Tailwind CSS ou CSS Modules ;
- éviter les styles globaux non maîtrisés ;
- ne pas styler par ID ;
- préférer les classes ;
- utiliser des noms explicites ;
- éviter la sur-spécificité ;
- approche mobile-first ;
- utiliser des tokens ou variables.

---

## 13. Accessibilité

Règles :

- utiliser le HTML sémantique ;
- respecter la hiérarchie des titres ;
- labels obligatoires sur les champs ;
- boutons accessibles au clavier ;
- alternatives textuelles pour les images utiles ;
- contraste suffisant.

---

## 14. Performance

Règles :

- lazy loading des routes ;
- pagination côté API pour les grandes listes ;
- debounce sur recherches ;
- skeleton loaders ;
- éviter les re-renders inutiles ;
- éviter les appels API redondants.

---

## 15. Tests frontend

Tests attendus :

- tests unitaires composants ;
- tests hooks / composables ;
- tests services API ;
- tests de formulaires ;
- tests end-to-end sur parcours MVP.

Outils recommandés :

```text
Vitest
Testing Library
Cypress ou Playwright
MSW
```

---

## 16. Git et commits

Règles :

- commits en anglais ;
- Conventional Commits ;
- message en lowercase ;
- une PR = un objectif clair.

Exemples :

```bash
git commit -m "feat(client): add account balance card"
git commit -m "fix(business): handle transfer api error"
git commit -m "test(client): add withdrawal form tests"
```

---

## 17. Checklist frontend avant merge

- [ ] Le composant a une responsabilité claire.
- [ ] Les appels API sont dans un service dédié.
- [ ] Les types TypeScript sont explicites.
- [ ] Aucun `any` non justifié.
- [ ] Les états loading, error et empty sont gérés.
- [ ] Les formulaires sont accessibles.
- [ ] Les erreurs API sont affichées correctement.
- [ ] Les composants critiques sont testés.
- [ ] Les styles ne cassent pas les autres pages.
- [ ] Le code est formaté avec Prettier.
