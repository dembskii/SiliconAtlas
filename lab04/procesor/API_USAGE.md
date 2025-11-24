# CPU REST API - Instrukcja użycia

## Uruchomienie aplikacji
```bash
./gradlew bootRun
```

## Endpointy API

### 1. GET - Pobierz wszystkie procesory
```bash
curl http://localhost:8080/api/cpu
```

**Response:** 200 OK
```json
[
  {
    "id": "uuid-string",
    "producer": "Intel Core i9-13900K",
    "cores": 24,
    "threads": 32,
    "frequencyGhz": 5.8
  },
  ...
]
```

### 2. GET - Pobierz procesor po ID
```bash
curl http://localhost:8080/api/cpu/{id}
```

**Response:** 200 OK (jeśli znaleziono)
```json
{
  "id": "uuid-string",
  "producer": "Intel Core i9-13900K",
  "cores": 24,
  "threads": 32,
  "frequencyGhz": 5.8
}
```

**Response:** 404 Not Found (jeśli nie znaleziono)
```json
{
  "timestamp": "2025-11-13T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "CPU not found with id: {id}"
}
```

### 3. POST - Dodaj nowy procesor
```bash
curl -X POST http://localhost:8080/api/cpu \
  -H "Content-Type: application/json" \
  -d '{
    "producer": "AMD Ryzen 5 5600X",
    "cores": 6,
    "threads": 12,
    "frequencyGhz": 4.6
  }'
```

**Response:** 201 Created
- **Header:** `Location: http://localhost:8080/api/cpu/{new-id}`
- **Body:** Utworzony obiekt CPU z wygenerowanym UUID

### 4. PUT - Aktualizuj procesor
```bash
curl -X PUT http://localhost:8080/api/cpu/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "producer": "AMD Ryzen 5 5600X Updated",
    "cores": 8,
    "threads": 16,
    "frequencyGhz": 4.8
  }'
```

**Response:** 200 OK (jeśli znaleziono)
```json
{
  "id": "original-uuid",
  "producer": "AMD Ryzen 5 5600X Updated",
  "cores": 8,
  "threads": 16,
  "frequencyGhz": 4.8
}
```

**Response:** 404 Not Found (jeśli nie znaleziono)

### 5. DELETE - Usuń procesor
```bash
curl -X DELETE http://localhost:8080/api/cpu/{id}
```

**Response:** 204 No Content (jeśli usunięto pomyślnie)

**Response:** 404 Not Found (jeśli nie znaleziono)

## Implementowane funkcjonalności

✅ **REST Controller** z pełnym CRUD
- GET `/api/cpu` - lista wszystkich procesorów
- GET `/api/cpu/{id}` - pojedynczy procesor po UUID
- POST `/api/cpu` - dodanie nowego procesora
- PUT `/api/cpu/{id}` - edycja procesora
- DELETE `/api/cpu/{id}` - usunięcie procesora

✅ **Baza danych** - `Map<String, Cpu>` w pamięci

✅ **CommandLineRunner** - inicjalizacja z 5 przykładowymi procesorami

✅ **Prawidłowe kody odpowiedzi HTTP:**
- 200 OK - pomyślne pobranie/aktualizacja
- 201 Created - pomyślne utworzenie zasobu
- 204 No Content - pomyślne usunięcie
- 404 Not Found - zasób nie został znaleziony
- 400 Bad Request - nieprawidłowe żądanie

✅ **Header Location** - ustawiany przy POST (zgodnie z RFC 7231)

✅ **Custom Exception** - `CpuNotFoundException` z globalnym handlerem

✅ **GlobalExceptionHandler** - centralna obsługa błędów

## Testowanie w Postman lub przeglądarce

1. Uruchom aplikację
2. Otwórz http://localhost:8080/api/cpu w przeglądarce, aby zobaczyć wszystkie procesory
3. Skopiuj jedno z UUID i użyj go do testowania pozostałych endpointów
