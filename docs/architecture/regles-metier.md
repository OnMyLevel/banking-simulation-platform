# Catalogue des Règles Métier

# Banking Simulation Platform

## 1. Gestion des utilisateurs

- **RB-USER-001** — Un utilisateur doit être authentifié avec un JWT valide pour accéder aux APIs sécurisées.
- **RB-USER-002** — Un utilisateur suspendu ne peut plus accéder à la plateforme.
- **RB-USER-003** — Un utilisateur possède au moins un rôle.
- **RB-USER-004** — Les rôles déterminent les permissions applicatives.

Rôles :

```text
ROLE_CLIENT
ROLE_BUSINESS
ROLE_ADVISOR
ROLE_ADMIN
```

---

## 2. Gestion des comptes bancaires

- **RB-ACCOUNT-001** — Un compte bancaire appartient à un seul propriétaire.
- **RB-ACCOUNT-002** — Chaque compte possède un IBAN unique.
- **RB-ACCOUNT-003** — Seul un compte actif peut effectuer des opérations.
- **RB-ACCOUNT-004** — Un compte clôturé est définitivement inactif.
- **RB-ACCOUNT-005** — Le solde ne peut être modifié que par une opération métier.

---

## 3. Gestion des dépôts

- **RB-DEPOSIT-001** — Un dépôt doit avoir un montant strictement positif.
- **RB-DEPOSIT-002** — Un dépôt important peut nécessiter une validation conseiller.
- **RB-DEPOSIT-003** — Un dépôt exécuté crédite le compte.
- **RB-DEPOSIT-004** — Tout dépôt doit être historisé et audité.

---

## 4. Gestion des retraits

- **RB-WITHDRAW-001** — Un retrait doit avoir un montant strictement positif.
- **RB-WITHDRAW-002** — Le solde disponible doit être suffisant.
- **RB-WITHDRAW-003** — Un retrait important peut nécessiter une validation conseiller.
- **RB-WITHDRAW-004** — Tout retrait doit être historisé et audité.

---

## 5. Gestion des virements

- **RB-TRANSFER-001** — Le compte émetteur doit être actif.
- **RB-TRANSFER-002** — Le solde du compte émetteur doit être suffisant.
- **RB-TRANSFER-003** — Un virement supérieur au seuil configurable nécessite une validation.
- **RB-TRANSFER-004** — Un virement refusé ne produit aucun mouvement financier.
- **RB-TRANSFER-005** — Un virement exécuté doit être historisé, audité et notifié.

---

## 6. Paiements instantanés

- **RB-INSTANT-001** — Un paiement instantané est exécuté immédiatement si les contrôles sont valides.
- **RB-INSTANT-002** — Un paiement instantané doit respecter les contrôles de compte actif et de solde suffisant.

---

## 7. Multi-virements entreprise

- **RB-BATCH-001** — Le multi-virement est réservé aux entreprises.
- **RB-BATCH-002** — Chaque ligne du fichier doit être validée.
- **RB-BATCH-003** — Le montant total du batch peut nécessiter une validation conseiller.
- **RB-BATCH-004** — Un batch refusé n'exécute aucun paiement.

---

## 8. Validations

- **RB-APPROVAL-001** — Une validation possède un état `PENDING`, `APPROVED` ou `REJECTED`.
- **RB-APPROVAL-002** — Un refus doit contenir un motif.
- **RB-APPROVAL-003** — Une opération approuvée devient exécutable.
- **RB-APPROVAL-004** — Une décision est irréversible.

---

## 9. Notifications

- **RB-NOTIF-001** — Une notification est générée après chaque opération importante.
- **RB-NOTIF-002** — Une notification est générée après validation ou refus.
- **RB-NOTIF-003** — Une notification doit être historisée.
- **RB-NOTIF-004** — L'échec d'une notification ne doit pas bloquer l'opération métier.

---

## 10. Audit et observabilité

- **RB-AUDIT-001** — Chaque opération bancaire doit être auditée.
- **RB-AUDIT-002** — Chaque décision conseiller doit être auditée.
- **RB-AUDIT-003** — Chaque action administrateur doit être auditée.
- **RB-AUDIT-004** — Les audits sont immuables.
- **RB-OBS-001** — Chaque appel API doit être tracé avec un correlationId.
- **RB-OBS-002** — Chaque erreur doit être journalisée.
- **RB-OBS-003** — Les logs sont centralisés via la plateforme d'observabilité.
