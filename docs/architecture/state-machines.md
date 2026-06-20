# États Métiers et State Machines

# Banking Simulation Platform

## 1. Objectif

Les state machines décrivent les états possibles des principaux objets métier et les transitions autorisées.

Elles servent à sécuriser :

- les règles métier ;
- les enums Java ;
- les contrôles fonctionnels ;
- les tests unitaires ;
- les tests d'intégration.

---

## 2. Cycle de vie d'un utilisateur

États :

```text
CREATED
ACTIVE
SUSPENDED
DELETED
```

```mermaid
stateDiagram-v2
    [*] --> CREATED
    CREATED --> ACTIVE: activation
    ACTIVE --> SUSPENDED: suspension
    SUSPENDED --> ACTIVE: réactivation
    ACTIVE --> DELETED: suppression logique
    SUSPENDED --> DELETED: suppression logique
    DELETED --> [*]
```

Règles :

- un utilisateur suspendu ne peut pas effectuer d'opération ;
- une suppression est logique ;
- toute suspension est auditée.

---

## 3. Cycle de vie d'un compte bancaire

États :

```text
CREATED
ACTIVE
SUSPENDED
CLOSED
ARCHIVED
```

```mermaid
stateDiagram-v2
    [*] --> CREATED
    CREATED --> ACTIVE: activation
    ACTIVE --> SUSPENDED: suspension
    SUSPENDED --> ACTIVE: réactivation
    ACTIVE --> CLOSED: clôture
    SUSPENDED --> CLOSED: clôture forcée
    CLOSED --> ARCHIVED: archivage
    ARCHIVED --> [*]
```

Règles :

- seul un compte actif peut effectuer des opérations ;
- un compte clôturé ne peut plus être réactivé ;
- toute clôture est historisée.

---

## 4. Cycle de vie d'une opération bancaire

États :

```text
CREATED
CHECKED
PENDING_APPROVAL
APPROVED
REJECTED
EXECUTED
FAILED
CANCELLED
NOTIFIED
ARCHIVED
```

```mermaid
stateDiagram-v2
    [*] --> CREATED
    CREATED --> CHECKED: contrôles métier

    CHECKED --> EXECUTED: sous seuil et valide
    CHECKED --> PENDING_APPROVAL: dépasse seuil
    CHECKED --> REJECTED: contrôle KO

    PENDING_APPROVAL --> APPROVED: conseiller valide
    PENDING_APPROVAL --> REJECTED: conseiller refuse

    APPROVED --> EXECUTED: exécution
    EXECUTED --> NOTIFIED: notification
    REJECTED --> NOTIFIED: notification refus
    FAILED --> NOTIFIED: notification échec

    CREATED --> CANCELLED: annulation utilisateur
    NOTIFIED --> ARCHIVED
    ARCHIVED --> [*]
```

Règles :

- une opération refusée ne peut pas être exécutée ;
- une opération exécutée ne revient jamais en attente ;
- tout refus doit contenir un motif ;
- toute opération doit produire un audit.

---

## 5. Cycle de vie d'un virement

```mermaid
stateDiagram-v2
    [*] --> DRAFT
    DRAFT --> CREATED: confirmation utilisateur
    CREATED --> CHECKED: vérification compte, solde, bénéficiaire

    CHECKED --> PENDING_APPROVAL: montant supérieur au seuil
    CHECKED --> EXECUTED: validation automatique
    CHECKED --> REJECTED: solde insuffisant ou compte inactif

    PENDING_APPROVAL --> APPROVED: validation conseiller
    PENDING_APPROVAL --> REJECTED: refus conseiller

    APPROVED --> EXECUTED: exécution
    EXECUTED --> NOTIFIED: confirmation
    REJECTED --> NOTIFIED: notification refus
    FAILED --> NOTIFIED: notification échec

    NOTIFIED --> ARCHIVED
    ARCHIVED --> [*]
```

---

## 6. Cycle de vie d'un multi-virement entreprise

```mermaid
stateDiagram-v2
    [*] --> DRAFT
    DRAFT --> IMPORTED: import CSV / Excel
    IMPORTED --> VALIDATED_FILE: fichier valide
    IMPORTED --> INVALID_FILE: fichier invalide

    VALIDATED_FILE --> PENDING_APPROVAL: demande validation conseiller
    INVALID_FILE --> NOTIFIED: notification erreur fichier

    PENDING_APPROVAL --> APPROVED: validation conseiller
    PENDING_APPROVAL --> REJECTED: refus conseiller

    APPROVED --> EXECUTING: lancement batch
    EXECUTING --> EXECUTED: toutes lignes exécutées
    EXECUTING --> PARTIALLY_EXECUTED: certaines lignes en erreur
    EXECUTING --> FAILED: erreur globale

    EXECUTED --> NOTIFIED
    PARTIALLY_EXECUTED --> NOTIFIED
    FAILED --> NOTIFIED
    REJECTED --> NOTIFIED

    NOTIFIED --> ARCHIVED
    ARCHIVED --> [*]
```

---

## 7. Cycle de vie d'une demande de validation

```mermaid
stateDiagram-v2
    [*] --> PENDING
    PENDING --> APPROVED: conseiller approuve
    PENDING --> REJECTED: conseiller refuse
    PENDING --> EXPIRED: délai dépassé
    PENDING --> CANCELLED: opération annulée
    APPROVED --> [*]
    REJECTED --> [*]
    EXPIRED --> [*]
    CANCELLED --> [*]
```

---

## 8. Cycle de vie d'une notification

```mermaid
stateDiagram-v2
    [*] --> CREATED
    CREATED --> READY_TO_SEND: préparation contenu
    READY_TO_SEND --> SENT: envoi réussi
    READY_TO_SEND --> FAILED: erreur d'envoi
    SENT --> READ: lecture utilisateur
    SENT --> ARCHIVED: archivage
    FAILED --> ARCHIVED: archivage erreur
    READ --> ARCHIVED
    ARCHIVED --> [*]
```

---

## 9. Synthèse

| Objet métier | États principaux |
|---|---|
| User | CREATED, ACTIVE, SUSPENDED, DELETED |
| Account | CREATED, ACTIVE, SUSPENDED, CLOSED, ARCHIVED |
| BankingOperation | CREATED, CHECKED, PENDING_APPROVAL, APPROVED, REJECTED, EXECUTED |
| Transfer | DRAFT, CREATED, CHECKED, PENDING_APPROVAL, EXECUTED |
| BatchPayment | DRAFT, IMPORTED, VALIDATED_FILE, PENDING_APPROVAL, EXECUTED |
| ApprovalRequest | PENDING, APPROVED, REJECTED, EXPIRED |
| Notification | CREATED, READY_TO_SEND, SENT, FAILED, READ |
