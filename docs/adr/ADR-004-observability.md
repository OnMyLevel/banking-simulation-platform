# ADR-004 — Observabilité dédiée

## Statut

Accepté

## Contexte

La plateforme doit tracer les opérations métier, les erreurs techniques, les appels API et les actions sensibles.

## Décision

Mettre en place une couche d'observabilité dédiée avec :

- audit métier ;
- logs techniques ;
- traces ;
- métriques ;
- correlationId ;
- stockage analytique ;
- archivage long terme.

## Raisons

- Centraliser la supervision.
- Séparer l'observabilité du métier bancaire.
- Faciliter le diagnostic technique.
- Préparer les tableaux de bord.

## Conséquences

Avantages :

- meilleure traçabilité ;
- meilleure capacité de debug ;
- architecture plus professionnelle.

Inconvénients :

- complexité supplémentaire ;
- nécessité de standardiser les logs et les événements.
