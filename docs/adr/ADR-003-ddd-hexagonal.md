# ADR-003 — Choix DDD et Architecture Hexagonale

## Statut

Accepté

## Contexte

Le projet doit démontrer une séparation claire entre métier, application et infrastructure.

## Décision

Chaque API backend suivra une architecture hexagonale :

```text
api/controller
application/usecase
domain/model
domain/service
domain/repository
infrastructure/persistence
infrastructure/security
config
```

Le domaine ne dépend pas directement de Spring Data JPA, des entités SQL ou des DTO REST.

## Raisons

- Protéger les règles métier.
- Faciliter les tests unitaires.
- Éviter le couplage direct entre métier et base de données.
- Rendre l'architecture plus professionnelle.

## Conséquences

Avantages :

- meilleure maintenabilité ;
- séparation claire des responsabilités ;
- tests plus simples.

Inconvénients :

- plus de classes ;
- plus de mappers ;
- courbe d'apprentissage plus forte.
