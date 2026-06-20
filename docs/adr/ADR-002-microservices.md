# ADR-002 — Choix d'une Architecture Microservices

## Statut

Accepté

## Contexte

La plateforme bancaire couvre plusieurs domaines métier distincts : utilisateurs, comptes, opérations bancaires, notifications et observabilité.

## Décision

Le backend est découpé en plusieurs APIs :

- API Gateway ;
- User Management API ;
- Account Banking API ;
- Core Banking API ;
- Notification API ;
- Observability API.

## Raisons

- Séparer les responsabilités métier.
- Faciliter l'évolution indépendante des domaines.
- Démontrer une architecture bancaire moderne.
- Préparer l'intégration future de Kafka, Kubernetes et CI/CD.

## Conséquences

Avantages :

- meilleure séparation fonctionnelle ;
- meilleure scalabilité ;
- alignement avec DDD.

Inconvénients :

- complexité réseau ;
- gestion des contrats interservices ;
- besoin d'observabilité plus forte.
