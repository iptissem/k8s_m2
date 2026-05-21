# Projet M2Cloud – Déploiement Kubernetes d’une architecture microservices

## Description

Ce projet consiste à déployer une application composée de plusieurs microservices sur Kubernetes afin d'assurer :

- haute disponibilité
- scalabilité automatique
- monitoring
- centralisation des logs
- sécurité réseau
- déploiement automatisé

L’infrastructure a été déployée avec Kubernetes et automatisée avec Helm.

---

## Architecture

L’application contient plusieurs services :

- webmvc → Frontend Web
- applicants.api → Gestion candidats
- jobs.api → Gestion offres
- identity.api → Authentification
- rabbitmq → Bus de communication
- sql.data → Base SQL Server
- user.data → Redis

Relations :

webmvc
├── applicants.api
├── jobs.api
└── identity.api

applicants.api
├── sql.data
└── rabbitmq

jobs.api
├── sql.data
└── rabbitmq

identity.api
├── user.data
└── rabbitmq

---

## Technologies utilisées

### Conteneurisation

- Docker
- Docker Hub

### Orchestration

- Kubernetes
- Helm

### Monitoring

- Prometheus
- Grafana
- Metrics Server
- kube-state-metrics

### Logs

- Elasticsearch
- FluentBit
- Kibana

### Sécurité

- TLS HTTPS
- ServiceAccount
- RBAC
- NetworkPolicy

---

## Déploiement

Déploiement Kubernetes classique :

```bash
kubectl apply -f k8s/
```

Déploiement Helm :

```bash
helm install projet ./test-helm
```

---

## Vérifications

Afficher les pods :

```bash
kubectl get pods -A
```

Afficher les services :

```bash
kubectl get svc -A
```

Afficher les ressources :

```bash
kubectl top pods
```

Afficher les événements :

```bash
kubectl get events -A
```

---

## Interfaces

| Service | URL |
|-----------|------|
| Frontend Web | http://localhost:8080 |
| Grafana | http://localhost:3000 |
| Prometheus | http://localhost:9090 |
| Kibana | http://localhost:5601 |
| RabbitMQ | http://localhost:15672 |

Identifiants :

Grafana :

admin/admin

RabbitMQ :

guest/guest

---

## Fonctionnalités implémentées

- Déploiement des microservices
- Monitoring cluster
- Centralisation des logs
- HPA
- Liveness Probe
- Readiness Probe
- Gestion des ressources CPU/Mémoire
- Ingress
- Déploiement Helm
- NetworkPolicy
- RBAC

---

## Membres du groupe

- Iptissem
- Redwan
- Lamya
- Klein
