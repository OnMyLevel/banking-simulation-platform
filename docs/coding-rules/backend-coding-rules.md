# Backend Coding Rules

# Banking Simulation Platform

## 1. Objectif

Ce document définit les règles de développement backend pour les APIs Spring Boot de la Banking Simulation Platform.

Il s'applique aux services suivants :

```text
api-gateway
user-management-api
account-banking-api
core-banking-api
notification-api
observability-api
```

Ces règles s'appuient sur les bonnes pratiques REST, API First, DDD, architecture hexagonale, sécurité, observabilité et maintenabilité.

Références principales :

- Microsoft Azure Architecture Center — RESTful Web API Design Best Practices
- Zalando RESTful API and Event Guidelines

---

## 2. Principes backend obligatoires

Chaque API doit respecter les principes suivants :

- API First ;
- RESTful design ;
- OpenAPI documenté ;
- architecture hexagonale ;
- séparation domaine / application / infrastructure ;
- sécurité par défaut ;
- observabilité par défaut ;
- tests automatisés ;
- logs structurés ;
- gestion d'erreurs standardisée.

---

## 3. Architecture interne d'un service

Chaque microservice Spring Boot doit suivre cette structure :

```text
src/main/java/com/banking/<service>/
├── api/
│   ├── controller/
│   ├── request/
│   └── response/
├── application/
│   ├── usecase/
│   ├── command/
│   └── query/
├── domain/
│   ├── model/
│   ├── valueobject/
│   ├── service/
│   ├── event/
│   └── repository/
├── infrastructure/
│   ├── persistence/
│   │   ├── entity/
│   │   ├── mapper/
│   │   ├── adapter/
│   │   └── jpa/
│   ├── security/
│   ├── client/
│   └── observability/
└── config/
```

Règle importante :

```text
Le domaine ne dépend jamais de Spring, JPA, REST, Kafka ou d'un framework technique.
```

---

## 4. Règles de nommage Java

| Élément | Règle | Exemple |
|---|---|---|
| Package | lowercase | `com.banking.account.domain.model` |
| Classe | PascalCase | `AccountService` |
| Interface | PascalCase | `AccountRepository` |
| Méthode | camelCase | `createAccount()` |
| Variable | camelCase | `accountId` |
| Constante | UPPER_SNAKE_CASE | `MAX_TRANSFER_AMOUNT` |
| Enum | PascalCase | `AccountStatus` |
| Valeur enum | UPPER_SNAKE_CASE | `PENDING_APPROVAL` |

---

## 5. Règles REST API

### 5.1 Les APIs sont orientées ressources

Utiliser des noms de ressources, pas des verbes.

Bon :

```http
GET /accounts
POST /accounts
GET /accounts/{account_id}
POST /transfers
```

Mauvais :

```http
POST /create-account
POST /execute-transfer
GET /get-user-details
```

---

### 5.2 Utiliser des noms pluriels

Les collections doivent être au pluriel.

```http
/users
/accounts
/transfers
/notifications
/audit-events
```

---

### 5.3 Utiliser le kebab-case dans les chemins

```http
/account-statements
/approval-requests
/audit-events
```

Ne pas utiliser :

```http
/accountStatements
/approval_requests
/AuditEvents
```

---

### 5.4 Utiliser snake_case pour les query parameters

Bon :

```http
GET /transfers?account_id=123&created_after=2026-01-01&status=EXECUTED
```

Mauvais :

```http
GET /transfers?accountId=123&createdAfter=2026-01-01
```

---

### 5.5 Éviter les URLs trop profondes

Maximum recommandé : trois niveaux de sous-ressources.

Acceptable :

```http
GET /customers/{customer_id}/accounts
GET /accounts/{account_id}/transactions
```

À éviter :

```http
GET /customers/{customer_id}/accounts/{account_id}/transactions/{transaction_id}/events
```

---

## 6. Méthodes HTTP

| Méthode | Usage |
|---|---|
| GET | Lire une ressource ou collection |
| POST | Créer une ressource ou déclencher un traitement métier |
| PUT | Remplacer une ressource complète, idempotent |
| PATCH | Modifier partiellement une ressource |
| DELETE | Supprimer ou clôturer logiquement une ressource |

Règles :

- `GET` ne modifie jamais l'état serveur ;
- `PUT` doit être idempotent ;
- `DELETE` doit privilégier la suppression logique pour les données bancaires ;
- `POST` peut être utilisé pour les opérations métier non idempotentes ;
- les traitements longs doivent retourner `202 Accepted` avec un endpoint de suivi.

---

## 7. Codes HTTP standards

| Code | Usage |
|---|---|
| 200 OK | Lecture ou traitement réussi |
| 201 Created | Ressource créée |
| 202 Accepted | Traitement accepté mais non terminé |
| 204 No Content | Succès sans corps de réponse |
| 400 Bad Request | Requête invalide |
| 401 Unauthorized | Authentification absente ou invalide |
| 403 Forbidden | Authentifié mais non autorisé |
| 404 Not Found | Ressource inexistante |
| 409 Conflict | Conflit métier ou état incompatible |
| 415 Unsupported Media Type | Format non supporté |
| 422 Unprocessable Entity | Règle métier non respectée |
| 429 Too Many Requests | Rate limit atteint |
| 500 Internal Server Error | Erreur technique non prévue |

---

## 8. Format des réponses JSON

Les réponses doivent être stables, lisibles et prévisibles.

Exemple ressource :

```json
{
  "id": "acc_123456",
  "iban": "FR7612345678901234567890185",
  "status": "ACTIVE",
  "currency": "EUR",
  "balance": {
    "amount": "1200.50",
    "currency": "EUR"
  },
  "created_at": "2026-06-20T10:15:30Z"
}
```

Règles :

- JSON uniquement pour les APIs métier ;
- propriétés JSON en `snake_case` ;
- dates en ISO 8601 UTC ;
- montants financiers en string ou decimal contrôlé ;
- devise en ISO 4217 ;
- enums en UPPER_SNAKE_CASE ;
- ne jamais exposer une entité JPA directement.

---

## 9. Gestion des erreurs

Toutes les erreurs doivent respecter un format commun.

```json
{
  "type": "https://banking-simulation/errors/insufficient-funds",
  "title": "Insufficient funds",
  "status": 422,
  "detail": "The account balance is not sufficient to execute this withdrawal.",
  "instance": "/withdrawals/wdr_123456",
  "code": "INSUFFICIENT_FUNDS",
  "correlation_id": "cor_789456"
}
```

Règles :

- ne jamais exposer de stack trace au client ;
- toujours retourner un `correlation_id` ;
- utiliser un code métier stable ;
- documenter les erreurs dans OpenAPI ;
- distinguer erreur technique et erreur métier.

---

## 10. Pagination, tri et filtrage

Toutes les collections exposées doivent supporter la pagination.

Exemple :

```http
GET /transactions?account_id=acc_123&limit=25&offset=0&sort=-created_at
```

Règles :

- `limit` obligatoire avec valeur par défaut ;
- `offset` accepté pour le MVP ;
- pagination par curseur possible dans une version avancée ;
- imposer une limite maximale ;
- filtrage explicite par query parameters ;
- tri via `sort`.

Réponse paginée :

```json
{
  "items": [],
  "pagination": {
    "limit": 25,
    "offset": 0,
    "next_offset": 25
  }
}
```

---

## 11. Idempotence

Les opérations sensibles doivent prévoir l'idempotence.

Header recommandé :

```http
Idempotency-Key: 0f6f3e64-62b7-4e2c-a4b6-4f51d3f7a900
```

À appliquer sur :

- dépôts ;
- retraits ;
- virements ;
- paiements instantanés ;
- multi-virements ;
- validations sensibles.

Règle :

```text
Une même requête avec la même Idempotency-Key ne doit pas produire deux opérations financières.
```

---

## 12. Sécurité backend

Chaque endpoint doit être sécurisé par défaut.

Règles :

- OAuth2 / OIDC / JWT ;
- validation du token côté API Gateway et/ou service ;
- RBAC via rôles applicatifs ;
- contrôle métier côté service, pas seulement côté frontend ;
- aucun secret dans le code ;
- mots de passe jamais loggés ;
- données sensibles masquées dans les logs.

Rôles :

```text
ROLE_CLIENT
ROLE_BUSINESS
ROLE_ADVISOR
ROLE_ADMIN
```

---

## 13. Validation des entrées

Chaque entrée API doit être validée.

Règles :

- Bean Validation obligatoire sur les DTO request ;
- validation métier dans le domaine ou application service ;
- aucune confiance envers les données du frontend ;
- messages d'erreur explicites ;
- contraintes OpenAPI alignées avec les validations Java.

Exemple :

```java
public record CreateAccountRequest(
    @NotBlank String ownerId,
    @NotBlank String currency
) {}
```

---

## 14. DTO, entities et domain models

Séparation obligatoire :

```text
Request DTO != Domain Model != JPA Entity != Response DTO
```

Règles :

- les controllers manipulent des DTO ;
- l'application layer manipule des commands / queries ;
- le domain layer manipule des objets métier ;
- l'infrastructure manipule les entités JPA ;
- les mappers sont explicites.

---

## 15. Repositories

Le domaine expose une interface repository.

```java
public interface AccountRepository {
    Optional<Account> findById(AccountId accountId);
    Account save(Account account);
}
```

L'implémentation JPA reste dans l'infrastructure.

```text
domain/repository/AccountRepository.java
infrastructure/persistence/adapter/JpaAccountRepositoryAdapter.java
infrastructure/persistence/jpa/SpringDataAccountJpaRepository.java
```

---

## 16. Transactions

Règles :

- transaction applicative au niveau use case ;
- pas de transaction ouverte dans les controllers ;
- pas de logique métier dans les repositories ;
- une opération financière doit être atomique ;
- les événements externes doivent être publiés après validation de la transaction.

---

## 17. Observabilité backend

Chaque API doit produire :

- logs structurés ;
- audit métier ;
- correlationId ;
- métriques ;
- traces techniques.

Champs minimaux :

```json
{
  "timestamp": "2026-06-20T10:15:30Z",
  "level": "INFO",
  "service": "core-banking-api",
  "correlation_id": "cor_123456",
  "user_id": "usr_123456",
  "action": "TRANSFER_CREATED",
  "result": "SUCCESS"
}
```

Règles :

- aucun log de mot de passe, token ou secret ;
- masquer IBAN complet si nécessaire ;
- tracer les opérations sensibles ;
- un audit métier ne doit pas être modifiable ;
- une erreur d'observabilité ne doit pas bloquer le métier.

---

## 18. Tests backend

Niveaux attendus :

- tests unitaires domaine ;
- tests application use cases ;
- tests controllers ;
- tests repositories ;
- tests d'intégration avec Testcontainers ;
- tests de contrat OpenAPI à terme.

Outils recommandés :

```text
JUnit 5
Mockito
AssertJ
Testcontainers
Spring Boot Test
REST Assured
```

Règles :

- tester les règles métier dans le domaine ;
- tester les transitions d'état ;
- tester les erreurs métier ;
- tester l'idempotence des opérations financières ;
- ne pas dépendre d'une base locale installée manuellement.

---

## 19. Documentation API

Chaque API doit exposer ou fournir :

- OpenAPI YAML ;
- description du scope ;
- exemples de requêtes ;
- exemples de réponses ;
- erreurs possibles ;
- règles d'autorisation ;
- pagination ;
- filtres ;
- événements produits.

Emplacement recommandé :

```text
docs/api-contracts/<service-name>/openapi.yaml
```

---

## 20. Git et commits

Règles :

- commits en anglais ;
- Conventional Commits ;
- messages en lowercase sauf nom technique entre backticks ;
- une PR = un objectif clair ;
- pas de mélange backend/frontend/infra sans raison.

Exemples :

```bash
git commit -m "feat(user): add create user use case"
git commit -m "fix(account): prevent closed account debit"
git commit -m "docs(api): add transfer endpoint contract"
```

---

## 21. Checklist backend avant merge

Avant de merger une PR backend :

- [ ] Le code respecte l'architecture hexagonale.
- [ ] Les DTO ne sont pas des entités JPA.
- [ ] Les endpoints sont documentés.
- [ ] Les erreurs suivent le format standard.
- [ ] Les tests unitaires passent.
- [ ] Les règles métier sont testées.
- [ ] Les logs ne contiennent pas de données sensibles.
- [ ] Le correlationId est propagé.
- [ ] Les endpoints sensibles sont sécurisés.
- [ ] Les noms d'URLs sont orientés ressources.
