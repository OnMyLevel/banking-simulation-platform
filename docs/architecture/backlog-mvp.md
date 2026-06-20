# Product Backlog MVP v1

# Banking Simulation Platform

## 1. Objectif du MVP

Le MVP v1 permet de livrer une première version fonctionnelle de la plateforme bancaire.

Objectifs :

- authentification ;
- gestion utilisateurs ;
- gestion entreprises ;
- création de comptes ;
- consultation solde ;
- consultation historique ;
- dépôt ;
- retrait ;
- virement SEPA ;
- virement SWIFT ;
- paiement instantané ;
- notifications ;
- audit ;
- lancement local avec Docker Compose.

---

## 2. Périmètre technique MVP v1

APIs incluses :

- API Gateway ;
- User Management API ;
- Account Banking API ;
- Core Banking API ;
- Notification API ;
- Observability API.

Frontends inclus :

- Client Banking Portal React ;
- Business Banking Portal Vue.

Hors MVP v1 :

- Angular Advisor/Admin Portal ;
- Kafka ;
- Kubernetes ;
- Terraform ;
- ClickHouse avancé ;
- Tsuga ;
- multi-virements ;
- validation conseiller avancée.

---

## 3. Épics MVP

| Epic | Description | Priorité |
|---|---|---|
| EPIC-01 | Authentification | Critique |
| EPIC-02 | Gestion utilisateurs | Critique |
| EPIC-03 | Gestion comptes | Critique |
| EPIC-04 | Dépôts | Haute |
| EPIC-05 | Retraits | Haute |
| EPIC-06 | Virements | Haute |
| EPIC-07 | Notifications | Moyenne |
| EPIC-08 | Audit | Moyenne |
| EPIC-09 | Frontend React Client | Haute |
| EPIC-10 | Frontend Vue Business | Moyenne |
| EPIC-11 | Docker / environnement local | Critique |

---

## 4. User Stories principales

### EPIC-01 — Authentification

- US-001 — Se connecter.
- US-002 — Se déconnecter.
- US-003 — Consulter son profil.

### EPIC-02 — Gestion utilisateurs

- US-004 — Créer un client particulier.
- US-005 — Modifier un client particulier.
- US-006 — Consulter un client particulier.
- US-007 — Créer une entreprise.
- US-008 — Associer un utilisateur à une entreprise.
- US-009 — Consulter une entreprise.
- US-010 — Attribuer un rôle.
- US-011 — Consulter les rôles.

### EPIC-03 — Gestion comptes

- US-012 — Créer un compte bancaire.
- US-013 — Consulter un compte.
- US-014 — Lister les comptes d'un utilisateur.
- US-015 — Consulter le solde.
- US-016 — Consulter l'historique.

### EPIC-04 — Dépôts

- US-017 — Créer un dépôt.
- US-018 — Consulter le détail d'un dépôt.
- US-019 — Historiser le dépôt.

### EPIC-05 — Retraits

- US-020 — Créer un retrait.
- US-021 — Vérifier le solde.
- US-022 — Historiser le retrait.

### EPIC-06 — Virements

- US-023 — Créer un virement SEPA.
- US-024 — Consulter un virement SEPA.
- US-025 — Créer un virement SWIFT.
- US-026 — Contrôler le BIC.
- US-027 — Créer un paiement instantané.
- US-028 — Recevoir une confirmation immédiate.

### EPIC-07 — Notifications

- US-029 — Créer une notification.
- US-030 — Consulter les notifications.
- US-031 — Marquer une notification comme lue.
- US-032 — Envoyer un email de confirmation.
- US-033 — Historiser l'email.

### EPIC-08 — Audit

- US-034 — Auditer la création d'un compte.
- US-035 — Auditer un dépôt.
- US-036 — Auditer un retrait.
- US-037 — Auditer un virement.

### EPIC-09 — Frontend React Client

- US-038 — Afficher le solde.
- US-039 — Afficher les dernières opérations.
- US-040 — Afficher les notifications.
- US-041 — Créer un dépôt.
- US-042 — Créer un retrait.
- US-043 — Créer un virement.

### EPIC-10 — Frontend Vue Business

- US-044 — Afficher les comptes professionnels.
- US-045 — Afficher l'historique.
- US-046 — Afficher les paiements.

### EPIC-11 — Infrastructure Docker

- US-047 — Dockeriser les APIs.
- US-048 — Dockeriser les frontends.
- US-049 — Créer un docker-compose local.

---

## 5. Découpage sprint recommandé

| Sprint | Objectif |
|---|---|
| Sprint 1 | Authentification, utilisateurs, rôles |
| Sprint 2 | Comptes, soldes, historique |
| Sprint 3 | Dépôts et retraits |
| Sprint 4 | Virements SEPA, SWIFT, instant payment |
| Sprint 5 | Notifications et audit |
| Sprint 6 | Frontend React et Vue |
| Sprint 7 | Docker, docker-compose, stabilisation |

---

## 6. Critères de validation MVP

Le MVP est validé si :

- un utilisateur peut se connecter ;
- un compte bancaire peut être créé ;
- le solde peut être consulté ;
- l'historique peut être consulté ;
- un dépôt peut être effectué ;
- un retrait peut être effectué ;
- un virement SEPA peut être effectué ;
- un virement SWIFT peut être effectué ;
- un paiement instantané peut être effectué ;
- une notification est générée ;
- un audit est enregistré ;
- l'environnement local démarre avec Docker Compose.
