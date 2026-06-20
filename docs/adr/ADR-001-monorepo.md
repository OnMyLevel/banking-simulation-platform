# ADR-001 — Choix du Monorepo

## Statut

Accepté

## Contexte

La plateforme contient plusieurs APIs backend, plusieurs frontends et une infrastructure complète.

## Décision

Le projet démarre avec une organisation en monorepo.

## Raisons

- Simplifier le démarrage du projet.
- Centraliser la documentation.
- Faciliter les évolutions transverses.
- Simplifier Docker Compose et les pipelines CI.
- Donner une vision claire du système complet.

## Conséquences

Avantages :

- structure lisible ;
- onboarding plus simple ;
- cohérence entre backend, frontend et infrastructure.

Inconvénients :

- le repo peut devenir volumineux ;
- il faudra organiser les pipelines par dossier.
