# Projet M2Cloud – Déploiement Kubernetes d’une architecture microservices

## Description

Ce projet consiste à déployer une architecture microservices dans un environnement Kubernetes afin de garantir :

- Haute disponibilité
- Scalabilité automatique
- Monitoring du cluster
- Centralisation des logs
- Sécurité réseau
- Déploiement automatisé

L'application est déployée sur Kubernetes et automatisée avec Helm.

Le projet a été testé sur Kubernetes Docker Desktop (cluster local).

---

# Architecture applicative

## Documentation d'architecture

📄 [Voir l'architecture complète (PDF)](architecture.drawio.pdf)

## Workflow utilisateur

Le schéma ci-dessous représente le parcours d'une requête utilisateur dans l'application :

<p align="center">
  <img src="workflow_user.png" width="900">
</p>

L'application contient les microservices suivants :

| Service | Description |
|-----------|-------------|
| webmvc | Frontend Web |
| applicants.api | Gestion des candidats |
| jobs.api | Gestion des offres |
| identity.api | Gestion authentification |
| sql.data | Base SQL Server |
| user.data | Redis |
| rabbitmq | Communication entre services |
| prometheus | Monitoring |
| grafana | Dashboards métriques |
| elasticsearch | Stockage des logs |
| fluent-bit | Collecte des logs |
| kibana | Visualisation des logs |

---

## Relations entre services

```txt
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

Prometheus
├── webmvc
├── applicants.api
├── jobs.api
└── identity.api

Fluent-bit
└── collecte les logs de tous les pods
```

---

# Architecture Kubernetes

Namespaces utilisés :

| Namespace | Description |
|------------|-------------|
| projet-apps | Applications |
| ns-data | Bases de données et messaging |
| ns-monitoring | Monitoring et logs |
| ingress-nginx | Contrôleur ingress |

---

# Technologies utilisées

## Conteneurisation

- Docker
- Docker Hub

## Orchestration

- Kubernetes
- Helm

## Monitoring

- Prometheus
- Grafana
- Metrics Server
- kube-state-metrics

## Logs

- Elasticsearch
- Fluent-bit
- Kibana

## Sécurité

- ServiceAccount
- RBAC
- NetworkPolicy
- TLS

---

# Structure du dépôt

```txt
.
├── Database/
├── Services/
├── Web/
├── k8s/
├── logging/
├── test-helm/
├── AUTHORS.TXT
├── docker-compose.yml
├── README.md
├── all.ps1
```

Description :

| Dossier | Description |
|-----------|-------------|
| Database | SQL Server |
| Services | APIs |
| Web | Frontend |
| k8s | Manifests Kubernetes |
| logging | Stack EFK |
| test-helm | Chart Helm |
| AUTHORS.TXT | Membres du groupe |
| all.ps1 | Script automatisation |

---

# Ports et dépendances

## Services applicatifs

| Service | Port conteneur | Port local | Dépendances |
|-----------|--------------|------------|-------------|
| webmvc | 80 | 8080 | applicants.api jobs.api identity.api |
| applicants.api | 80 | 8081 | sql.data rabbitmq |
| jobs.api | 80 | 8083 | sql.data rabbitmq |
| identity.api | 80 | 8084 | user.data rabbitmq |

## Services données

| Service | Port |
|-----------|-------|
| SQL Server | 1433 |
| Redis | 6379 |
| RabbitMQ | 5672 |
| RabbitMQ UI | 15672 |

## Monitoring

| Service | Port |
|-----------|------|
| Prometheus | 9090 |
| Grafana | 3000 |
| Elasticsearch | 9200 |
| Kibana | 5601 |

---

# Build des images Docker

Exemples :

```bash
docker build -t webmvc -f Web/Dockerfile .
docker build -t applicants-api -f Services/Applicants.Api/Dockerfile .
docker build -t jobs-api -f Services/Jobs.Api/Dockerfile .
docker build -t identity-api -f Services/Identity.Api/Dockerfile .
docker build -t sql-data -f Database/Dockerfile ./Database
```

Push Docker Hub :

```bash
docker tag webmvc username/webmvc:v1
docker push username/webmvc:v1
```

---

# Déploiement Kubernetes classique

Déploiement manuel :

```bash
kubectl apply -f k8s/
kubectl apply -f logging/
```

Vérifier :

```bash
kubectl get pods -A
kubectl get svc -A
```

---

# Déploiement Helm

Positionnement :

```bash
cd test-helm
```

Vérifier le chart :

```bash
helm lint .
```

Générer les manifests :

```bash
helm template k8s . -n projet-apps > rendered.yaml
```

Déployer :

```bash
helm upgrade --install k8s . \
--namespace projet-apps \
--create-namespace
```

Vérifier :

```bash
helm list -A
```

---

# Vérification du cluster

Afficher tous les pods :

```bash
kubectl get pods -A
```

Afficher les services :

```bash
kubectl get svc -A
```

Afficher les ingress :

```bash
kubectl get ingress -A
```

Afficher les ressources :

```bash
kubectl top pods -A
kubectl top nodes
```

Afficher les événements :

```bash
kubectl get events -A
```

Afficher les logs :

```bash
kubectl logs <pod> -n <namespace>
```

---

# Monitoring

Le monitoring est assuré par :

- Metrics Server
- Prometheus
- Grafana
- kube-state-metrics

Vérification :

```bash
kubectl get pods -n ns-monitoring
```

---

# Gestion des logs

Stack utilisée :

- Elasticsearch
- Fluent-bit
- Kibana

Vérification :

```bash
kubectl logs daemonset/fluent-bit -n ns-monitoring
```

---

# HPA

Le projet implémente un Horizontal Pod Autoscaler basé sur la consommation CPU.

Vérification :

```bash
kubectl get hpa -A
```

---

# Sécurité

Éléments implémentés :

- ServiceAccount dédié
- RBAC
- NetworkPolicy
- HTTPS via Ingress
- Certificat auto-signé TLS

Vérification :

```bash
kubectl get serviceaccount -A
kubectl get role -A
kubectl get rolebinding -A
kubectl get networkpolicy -A
```

---

# Accès aux interfaces

| Service | URL |
|-----------|-----|
| Frontend Web | https://localhost |
| Grafana | http://localhost:3000 |
| Prometheus | http://localhost:9090 |
| Kibana | http://localhost:5601 |
| RabbitMQ | http://localhost:15672 |

Identifiants :

### Grafana

```txt
admin
admin
```

### RabbitMQ

```txt
guest
guest
```

---

# Nettoyage

Supprimer le déploiement :

```bash
helm uninstall k8s -n projet-apps
```

Supprimer les namespaces :

```bash
kubectl delete namespace projet-apps
kubectl delete namespace ns-data
kubectl delete namespace ns-monitoring
```

---

# Limites connues

Le projet a été réalisé et testé sur Kubernetes Docker Desktop :

- cluster local single-node
- ressources limitées
- certains composants monitoring nécessitent beaucoup de RAM

Dans un environnement AKS multi-nœuds, la haute disponibilité serait pleinement exploitée.

---

### Matrice des flux applicatifs de JobServer

| Source | Destination | Fonction du flux |
|---|---|---|
| Utilisateur externe | webmvc | Accès HTTPS à l’application via l’Ingress |
| webmvc | applicants.api | Consultation et gestion des candidatures |
| webmvc | jobs.api | Consultation des offres d’emploi |
| webmvc | identity.api | Authentification des utilisateurs |
| applicants.api | sql.data | Accès aux données des candidatures |
| applicants.api | rabbitmq | Échanges entre services |
| jobs.api | sql.data | Accès aux données des offres |
| jobs.api | rabbitmq | Échanges entre services |
| identity.api | user.data | Accès aux données d’authentification |
| identity.api | rabbitmq | Échanges entre services |
| Prometheus | Services supervisés | Collecte des métriques |
| Fluent Bit | Pods du cluster | Collecte des logs |
| Kibana | Elasticsearch | Consultation et recherche dans les logs |
| Grafana | Prometheus | Visualisation des métriques |

## Annexe 7 – Organisation des équipes JobServer et prestataire

Cette annexe précise la répartition des rôles entre JobServer, en tant que client, et l’équipe prestataire chargée de la migration et de la phase de stabilisation de l’infrastructure Kubernetes.

## Annexe 7 – Organisation des équipes JobServer et prestataire

Cette annexe précise la répartition des rôles entre JobServer, en tant que client, et l’équipe prestataire chargée de la migration et de la phase de stabilisation de l’infrastructure Kubernetes.

| Organisation | Persona | Fonction | Objectif principal | Rôle en cas d’incident |
|---|---|---|---|---|
| JobServer | Claire Martin | Responsable métier / Commanditaire | Garantir la disponibilité du service pour les candidats et les recruteurs | Évalue l’impact métier et valide le retour au fonctionnement normal |
| JobServer | Lucas Bernard | Développeur référent | Maintenir le bon fonctionnement des microservices | Analyse et corrige les anomalies liées au code ou au comportement applicatif |
| Prestataire | Iptissem Zitouni | Cheffe de projet / Référente technique | Piloter la migration et assurer la phase de stabilisation | Qualifie l’incident, définit sa priorité, suit le FT et coordonne les différents intervenants |
| Prestataire | Mehdi Laurent | Ingénieur DevOps / Exploitation | Maintenir l’infrastructure Kubernetes opérationnelle | Analyse les incidents liés au cluster, aux pods, aux ressources, au réseau ou au déploiement |
| Prestataire | Sofia Robert | Référente sécurité | Garantir le respect des exigences de sécurité | Intervient sur les incidents liés aux accès, aux données ou à la sécurité du SI |

**Tableau A7.1 – Personae et responsabilités des équipes JobServer et prestataire**

Cette organisation me permet de ne pas traiter tous les incidents de la même manière. Je reste le point d’entrée pendant la phase de stabilisation, mais la résolution dépend ensuite de l’origine du problème. Un incident lié à Kubernetes ou au déploiement est pris en charge côté prestataire, tandis qu’une anomalie provenant du code d’un microservice est transmise à l’équipe de développement JobServer.

*Les noms utilisés pour les membres des équipes sont fictifs et servent uniquement à illustrer l’organisation retenue pour le projet.*

# Membres du groupe

Voir :

```txt
AUTHORS.TXT
```
