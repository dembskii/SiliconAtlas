# CPU Management System

Profesjonalny system zarządzania procesorami z REST API i zaawansowaną architekturą rozproszoną. Projekt demonstruje best practices w tworzeniu nowoczesnych aplikacji webowych na Spring Boot.

---

## Spis treści

- [Przegląd projektu](#przegląd-projektu)
- [Architektura](#architektura)
- [Technologie](#technologie)
- [API Documentation](#api-documentation)
- [Wymagania systemowe](#wymagania-systemowe)
- [Instalacja i uruchomienie](#instalacja-i-uruchomienie)
- [Testing](#testing)

---

## Przegląd projektu

CPU Management System to kompleksowa aplikacja webowa do zarządzania procesorami i technologiami produkcyjnymi. Obsługuje:

- **Zarządzanie katalogiem** procesorów z parametrami technicznymi
- **Administracja producentami** i ich historią
- **Zarządzanie technologiami** litografii (10nm, 7nm, 5nm, itp.)
- **Benchmarking** wydajności CPU (Passmark, Cinebench, multi-core score)
- **Uwierzytelnianie i autoryzacja** z JWT tokens
- **Ograniczanie częstości zapytań** (100 req/min per user)
- **Powiadomienia real-time** przez WebSocket
- **Event streaming** na Apache Kafka
- **Cache'owanie** z Redis dla optymalizacji

---

## Architektura

System wykorzystuje architekturę trójwarstwową z podziałem na:

```
┌─────────────────────────────────────────────────┐
│              Frontend (HTML/CSS/JS)             │
│         WebSocket & Fetch API Client            │
├─────────────────────────────────────────────────┤
│         Spring Boot REST API (Layer 4.0.0)      │
│    Controllers → Services → Repositories        │
├─────────────────────────────────────────────────┤
│  PostgreSQL | Redis Cache | Apache Kafka       │
│         Event-Driven Architecture               │
└─────────────────────────────────────────────────┘
```

**Przepływ danych:**
1. Frontend wysyła żądania HTTP do REST API
2. API waliduje JWT token i stosuje rate limiting
3. Serwisy przetwarzają logikę biznesową
4. Zmiany są utrwalane w PostgreSQL
5. Zdarzenia publikowane na Kafka
6. WebSocket wysyła notyfikacje do klientów
7. Redis cache przyspiesza odczyty

---

## Technologie

### Backend

| Komponent | Technologia | Wersja | Cel |
|-----------|-------------|--------|-----|
| **Język** | Java | 21 LTS | Nowoczesna wersja z virtual threads |
| **Framework** | Spring Boot | 4.0.0 | Application framework |
| **Build** | Gradle | 8.0+ | Automatyzacja budowy |

### Baza danych i storage

| Komponent | Technologia | Wersja | Cel |
|-----------|-------------|--------|-----|
| **RDBMS** | PostgreSQL | 16-alpine | Główna baza relacyjna |
| **ORM** | Hibernate 7 | - | Object-Relational Mapping |
| **Data Access** | Spring Data JPA | - | Abstrakcja dostępu do danych |
| **Cache** | Redis | 7-alpine | In-memory session/cache store |
| **Spring Cache** | - | - | Integracja caching'u |

### Bezpieczeństwo

| Komponent | Technologia | Wersja | Cel |
|-----------|-------------|--------|-----|
| **Security** | Spring Security | 7.0.0 | Framework zarządzania dostępem |
| **Authentication** | JWT (JJWT) | 0.12.3 | Bezstanowe uwierzytelnianie |
| **Hashing** | BCrypt | - | Bezpieczne hashing haseł |
| **Token** | Bearer Token | - | Authorization header |

### Komunikacja i Event Streaming

| Komponent | Technologia | Wersja | Cel |
|-----------|-------------|--------|-----|
| **Message Broker** | Apache Kafka | 7.5.0 | Event streaming |
| **Coordinator** | Zookeeper | 7.5.0 | Zarządzanie klastrem Kafka |
| **Spring Kafka** | - | 3.1.x | Producenci i konsumenci |
| **Real-time** | WebSocket | - | Dwukierunkowa komunikacja |
| **Messaging** | Spring Messaging | 7.0.1 | STOMP protocol |

### Serialization i Validacja

| Komponent | Technologia | Wersja | Cel |
|-----------|-------------|--------|-----|
| **JSON** | Jackson | 2.16+ | Serializacja/deserializacja |
| **Java Time** | Jackson Datatype JSR310 | - | Obsługa LocalDateTime |
| **Lombok** | - | 1.18.30 | Redukcja boilerplate'u |
| **Thymeleaf** | - | 3.1.x | Server-side template engine |

### Dodatkowe biblioteki

| Komponent | Technologia | Wersja | Cel |
|-----------|-------------|--------|-----|
| **Rate Limiting** | Bucket4j | 7.6.0 | Ograniczanie częstości API |
| **Testing** | JUnit 5 | 5.9.x | Unit testing framework |
| **Mocking** | Mockito | 5.1.x | Test doubles |
| **Test DB** | H2 Database | 2.2.224 | In-memory DB do testów |

### Dokumentacja API

| Komponent | Technologia | Wersja | Cel |
|-----------|-------------|--------|-----|
| **OpenAPI** | springdoc-openapi | 2.8.0 | Generowanie dokumentacji |
| **Swagger UI** | - | - | Interaktywny interfejs API |
| **ReDoc** | - | - | Alternatywny widok dokumentacji |

### Infrastruktura i Deployment

| Komponent | Technologia | Wersja | Cel |
|-----------|-------------|--------|-----|
| **Containerization** | Docker | 20.10+ | Pakowanie aplikacji |
| **Orchestration** | Docker Compose | 2.0+ | Zarządzanie kontenerami |
| **Monitoring** | Grafana | 10.x | Wizualizacja metryk |
| **Networking** | Docker Bridge | - | Izolowana sieć |

### Frontend

| Komponent | Technologia | Wersja | Cel |
|-----------|-------------|--------|-----|
| **Markup** | HTML5 | - | Struktura aplikacji |
| **Styling** | CSS3 | - | Responsywny design |
| **Logic** | JavaScript ES6+ | - | Interaktywność |

**Cechy frontendu:**
- Uwierzytelnianie JWT z local storage
- WebSocket client dla powiadomień
- Fetch API z automatycznym authorization headerem
- Notyfikacje toast
- Responsive design

---

## API Documentation

Projekt zawiera pełną dokumentację API wygenerowaną przez OpenAPI 3.0 (Swagger).

### Dostęp do dokumentacji

Automatycznie generowana dokumentacja jest dostępna pod:

- **Swagger UI (interaktywny)**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **ReDoc (alternatywny widok)**: [http://localhost:8080/v3/api-docs.yaml](http://localhost:8080/v3/api-docs.yaml)

### Endpoints REST API

**Zasoby:**
- `GET /api/v1/cpus` - Lista wszystkich procesorów
- `POST /api/v1/cpus` - Tworzenie nowego procesora
- `GET /api/v1/cpus/{id}` - Pobierz procesor po ID
- `PUT /api/v1/cpus/{id}` - Aktualizacja procesora
- `DELETE /api/v1/cpus/{id}` - Usunięcie procesora

**Zaawansowane:**
- `POST /api/v1/cpus/search` - Wyszukiwanie z kryteriami
- `GET /api/v1/cpus/benchmarks` - Porównanie wydajności
- `GET /api/v1/manufacturers` - Producenci
- `GET /api/v1/technologies` - Dostępne technologie

### Uwierzytelnianie

```bash
# Rejestracja
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user","email":"user@example.com","password":"pass123"}'

# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"pass123"}'

# Użycie tokenu
curl -X GET http://localhost:8080/api/v1/cpus \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Wymagania systemowe

### Do uruchomienia z Docker Compose

- Docker 20.10+
- Docker Compose 2.0+
- 8 GB RAM (PostgreSQL, Redis, Kafka)
- 2 GB wolnego miejsca na dysku

### Do lokalnego development (bez Docker)

- Java 21 JDK
- Gradle 8.0+
- PostgreSQL 16
- Redis 7
- Apache Kafka 7.5.0

---

## Instalacja i uruchomienie

### Opcja 1: Docker Compose (Rekomendowana)

```bash
# Klonowanie repozytorium
git clone <repository-url>
cd CpuManagementSystem

# Uruchomienie całego stacku
docker-compose up -d

# Sprawdzenie statusu
docker-compose ps

# Monitorowanie logów
docker-compose logs -f backend-spring-app
```

**Dostępne usługi:**
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- PostgreSQL: localhost:5432
- Redis: localhost:6379
- Kafka: localhost:9092
- Grafana: http://localhost:3000

### Opcja 2: Kompilacja i uruchomienie lokalne

```bash
# Przejście do folderu backend
cd backend-spring-app

# Kompilacja projektu
./gradlew clean build

# Uruchomienie (wymaga działającego PostgreSQL, Redis)
./gradlew bootRun

# URL aplikacji
# http://localhost:8080
```

### Konfiguracja zmiennych środowiskowych

Utwórz plik `.env`:

```properties
# PostgreSQL
POSTGRES_DB=cpu_management_db
POSTGRES_USER=cpu_admin
POSTGRES_PASSWORD=your_secure_password
POSTGRES_PORT=5432

# Redis
REDIS_PORT=6379

# Backend
BACKEND_PORT=8080
SPRING_PROFILES_ACTIVE=docker

# JWT
JWT_SECRET=your_super_secret_key_min_32_chars
JWT_EXPIRATION=86400000

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_CPU_MANAGEMENT=DEBUG
```

---

## Testing

### Uruchamianie testów

```bash
cd backend-spring-app

# Wszystkie testy
./gradlew test

# Konkretny pakiet
./gradlew test --tests "com.cpu.management.controller.*"

# Raport testów
# Plik: backend-spring-app/build/reports/tests/test/index.html
```

### Zasady testowania

- **Unit tests** dla serwisów i mapperów (Mockito)
- **Integration tests** dla kontrolerów (MockMvc)
- **In-memory database** (H2) dla testów
- **Dane testowe** za pomocą @DataJpaTest

### Zagęszczenie testów

```
Encje:        24 tests (POJO validation)
Mappery:      26 tests (DTO mapping)
Serwisy:      46 tests (Business logic + mocks)
Kontrolery:   22 tests (REST API + MockMvc)
Exceptions:    7 tests (Custom exceptions)
────────────────
Total:       136 tests
```

---

## Struktura projektu

```
CpuManagementSystem/
├── backend-spring-app/
│   ├── src/main/java/com/cpu/management/
│   │   ├── config/          # Konfiguracja Spring
│   │   ├── controller/      # REST endpoints
│   │   ├── domain/          # JPA entities
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── service/         # Business logic
│   │   ├── repository/      # Data access layer
│   │   ├── mapper/          # DTO mappers
│   │   ├── security/        # Bezpieczeństwo
│   │   ├── processor/       # Kafka consumers
│   │   └── specification/   # JPA criteria
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   ├── static/          # HTML, CSS, JS
│   │   └── templates/       # Thymeleaf templates
│   ├── src/test/            # Unit i integration tests
│   └── build.gradle         # Gradle configuration
├── docker-compose.yml       # Orkiestracja kontenerów
├── scripts/
│   └── init.sql            # Inicjalizacja bazy
└── README.md               # Ten pliki
```

---

## Wersje

- Wersja aplikacji: **1.0.0-SNAPSHOT**
- Spring Boot: **4.0.0**
- Java: **21 LTS**
- PostgreSQL: **16**
- Kafka: **7.5.0**

---

## Autor

Dominik Dembski
