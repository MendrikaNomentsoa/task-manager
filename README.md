# Task Manager API 🚀

Une API REST complète de gestion de tâches et projets construite avec Spring Boot 4.

## 📋 Table des matières

- [À propos](#à-propos)
- [Technologies](#technologies)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Lancer le projet](#lancer-le-projet)
- [Documentation API](#documentation-api)
- [Endpoints](#endpoints)
- [Tests](#tests)
- [Docker](#docker)
- [Structure du projet](#structure-du-projet)

---

## 📖 À propos

Task Manager API est une application backend qui permet de gérer des projets et des tâches, similaire à un Trello simplifié. Elle expose une API REST sécurisée par JWT.

### Fonctionnalités
- ✅ Gestion des utilisateurs (register, login)
- ✅ Gestion des projets (CRUD)
- ✅ Gestion des tâches (CRUD, statut, priorité)
- ✅ Authentification JWT
- ✅ Documentation Swagger
- ✅ Tests unitaires
- ✅ Dockerisé

---

## 🛠️ Technologies

| Technologie | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.0 |
| Spring Security | 6 |
| PostgreSQL | 16 |
| Flyway | 12 |
| JWT (jjwt) | 0.12.6 |
| Lombok | 1.18.x |
| Swagger / OpenAPI | 2.5.0 |
| Docker | - |
| JUnit | 6 |
| Mockito | 5 |

---

## ✅ Prérequis

- Java 21
- Maven 3.9+
- PostgreSQL 14+
- Docker + Docker Compose (optionnel)

---

## ⚙️ Installation

### 1. Clone le projet

```bash
git clone https://github.com/TON_USERNAME/task-manager.git
cd task-manager
```

### 2. Crée la base de données

```sql
CREATE DATABASE taskmanager;
```

### 3. Configure l'application

Copie le fichier exemple et remplis tes valeurs :

```bash
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties
```

Modifie `application.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanager
spring.datasource.username=postgres
spring.datasource.password=TON_MOT_DE_PASSE
jwt.secret=TON_SECRET_JWT
```

---

## 🚀 Lancer le projet

### Sans Docker

```bash
mvn spring-boot:run
```

### Avec Docker

```bash
docker-compose up --build
```

L'application démarre sur `http://localhost:8080`

---

## 📖 Documentation API

Une fois l'application lancée, accède à Swagger UI :
http://localhost:8080/swagger-ui.html

---

## 🌐 Endpoints

### Auth
| Méthode | URL | Description | Auth |
|---|---|---|---|
| POST | `/api/auth/register` | Créer un compte | ❌ |
| POST | `/api/auth/login` | Se connecter | ❌ |

### Users
| Méthode | URL | Description | Auth |
|---|---|---|---|
| GET | `/api/users` | Lister les utilisateurs | ✅ |
| GET | `/api/users/{id}` | Trouver un utilisateur | ✅ |
| POST | `/api/users` | Créer un utilisateur | ✅ |
| PUT | `/api/users/{id}` | Modifier un utilisateur | ✅ |
| DELETE | `/api/users/{id}` | Supprimer un utilisateur | ✅ |

### Projects
| Méthode | URL | Description | Auth |
|---|---|---|---|
| GET | `/api/projects` | Lister les projets | ✅ |
| GET | `/api/projects/{id}` | Trouver un projet | ✅ |
| GET | `/api/projects/search?name=x` | Rechercher un projet | ✅ |
| POST | `/api/projects` | Créer un projet | ✅ |
| PUT | `/api/projects/{id}` | Modifier un projet | ✅ |
| DELETE | `/api/projects/{id}` | Supprimer un projet | ✅ |

### Tasks
| Méthode | URL | Description | Auth |
|---|---|---|---|
| GET | `/api/tasks/{id}` | Trouver une tâche | ✅ |
| GET | `/api/tasks/project/{id}` | Tâches d'un projet | ✅ |
| POST | `/api/tasks?projectId=1` | Créer une tâche | ✅ |
| PATCH | `/api/tasks/{id}/status` | Changer le statut | ✅ |
| DELETE | `/api/tasks/{id}` | Supprimer une tâche | ✅ |

---

## 🔐 Authentification

Toutes les routes (sauf `/api/auth/**`) nécessitent un token JWT.

### 1. Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Jean","email":"jean@email.com","password":"secret123"}'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"jean@email.com","password":"secret123"}'
```

### 3. Utilise le token
```bash
curl http://localhost:8080/api/projects \
  -H "Authorization: Bearer TON_TOKEN"
```

---

## 🧪 Tests

```bash
# Lancer tous les tests
mvn test

# Résultats attendus
# UserServiceTest      - 7 tests
# ProjectServiceTest   - 5 tests
# TaskServiceTest      - 5 tests
# UserControllerTest   - 6 tests
# ProjectControllerTest- 7 tests
# TaskControllerTest   - 6 tests
# Total: 38 tests ✅
```

---

## 🐳 Docker

```bash
# Lancer en avant-plan
docker-compose up --build

# Lancer en arrière-plan
docker-compose up --build -d

# Voir les logs
docker-compose logs -f app

# Arrêter
docker-compose down

# Arrêter et supprimer les données
docker-compose down --volumes
```

---

## 📁 Structure du projet
src/main/java/com/taskmanager/

├── config/

│   ├── SecurityConfig.java

│   ├── SwaggerConfig.java

│   └── CorsConfig.java

├── controller/

│   ├── AuthController.java

│   ├── UserController.java

│   ├── ProjectController.java

│   └── TaskController.java

├── exception/

│   ├── ResourceNotFoundException.java

│   └── GlobalExceptionHandler.java

├── model/

│   ├── User.java

│   ├── Project.java

│   ├── Task.java

│   ├── TaskStatus.java

│   └── TaskPriority.java

├── repository/

│   ├── UserRepository.java

│   ├── ProjectRepository.java

│   └── TaskRepository.java

├── security/

│   ├── JwtUtil.java

│   ├── JwtFilter.java

│   └── UserDetailsServiceImpl.java

└── service/

├── UserService.java

├── ProjectService.java

└── TaskService.java

---

## 👤 RAKOTORISOA Ny Mendrika Nomentsoa— [GitHub](https://github.com/MendrikaNomentsoa)

---