# Documentation Technique - Arduino Simulator

## Architecture

### Vue d'ensemble
Le projet simule un système Arduino avec des capteurs et une LED contrôlable via une API REST Spring Boot.

```
┌─────────────────────────────────────────────────┐
│              Controllers (REST)                  │
│  LedController │ SensorController │ StatusController │
└─────────────┬───────────────────────────────────┘
              │
┌─────────────▼───────────────────────────────────┐
│                Services                          │
│        LedService    │    SensorService          │
└─────────────┬───────────────────────────────────┘
              │
┌─────────────▼───────────────────────────────────┐
│                  Models                          │
│    Sensor │ Threshold │ SensorType               │
└──────────────────────────────────────────────────┘
```

## Composants principaux

### 1. Models

#### Sensor
Représente un capteur physique (température ou luminosité).
- **Attributs**: id, name, type, pos
- **Méthode clé**: `readValue()` - génère des valeurs aléatoires réalistes

#### SensorType (Enum)
- `TEMPERATURE`: 15-30°C
- `LIGHT`: 0-1000 lux

#### Threshold
Représente un seuil de déclenchement pour la LED.
- **Attributs**: sensor (ID), val (valeur), mode (0=below, 1=above)
- **Méthode clé**: `isTriggered(sensorValue)` - vérifie si le seuil est atteint

### 2. Services

#### SensorService
Gère la collection de capteurs disponibles.
- Initialise 3 capteurs par défaut (2 température, 1 luminosité)
- Fournit des méthodes d'accès aux capteurs

#### LedService
Gère l'état de la LED et les seuils.
- **État**: on/off (0/1)
- **Position**: 13 (pin Arduino typique)
- Gère le threshold et vérifie automatiquement les conditions

### 3. Controllers

#### LedController
Endpoints pour contrôler la LED:
- `POST /led/on` - Allume
- `POST /led/off` - Éteint
- `POST /led/toggle` - Bascule
- `GET /led` - État actuel

Endpoints threshold:
- `POST /led/threshold` - Définir
- `GET /led/threshold` - Consulter
- `DELETE /led/threshold` - Supprimer

#### SensorController
Endpoint unique avec paramètres optionnels:
- `GET /sensor` - Tous les capteurs
- `GET /sensor?id=1` - Un capteur
- `GET /sensor?ids=1,2` - Plusieurs capteurs

#### StatusController
Endpoint combiné pour obtenir plusieurs infos en une requête:
- `GET /status?led=1&sensor_ids=1,2&threshold=1`

## Format des réponses

### Sensor
```json
{
  "id": 1,
  "name": "Temperature Sensor 1",
  "val": 23.45,
  "unit": "°C",
  "pos": 1
}
```

### LED
```json
{
  "status": 1,
  "pos": 13
}
```

### Threshold
```json
{
  "sensor": 1,
  "val": 25.0,
  "mode": 1
}
```

## Correspondance avec le code Arduino

| Arduino (C++) | Spring Boot (Java) |
|--------------|-------------------|
| `HandlerList::addHandler()` | `@RestController` + `@RequestMapping` |
| `LedService::getInstance()` | `@Autowired LedService` |
| `SensorManager::getInstance()` | `@Autowired SensorService` |
| `StaticJsonDocument<>` | Classes DTO (Response/Request) |
| `server.send()` | `ResponseEntity.ok()` |
| `WebServer& server` | `@RequestParam`, `@RequestBody` |

## Configuration

### application.properties
```properties
server.port=8080
spring.application.name=Arduino Simulator
logging.level.com.arduino.simulator=INFO
```

### Capteurs par défaut
```java
sensors.put(1, new Sensor(1, "Temperature Sensor 1", SensorType.TEMPERATURE, 1));
sensors.put(2, new Sensor(2, "Light Sensor 1", SensorType.LIGHT, 2));
sensors.put(3, new Sensor(3, "Temperature Sensor 2", SensorType.TEMPERATURE, 3));
```

## Simulation des valeurs

### Température
```java
return 15 + random.nextDouble() * 15; // 15-30°C
```

### Luminosité
```java
return random.nextDouble() * 1000; // 0-1000 lux
```

## Fonctionnement du Threshold

1. Un threshold est défini avec un capteur, une valeur et un mode
2. Quand `checkThreshold()` est appelé:
   - Lit la valeur actuelle du capteur
   - Compare avec le seuil selon le mode
   - Allume ou éteint la LED automatiquement

### Exemple
```
Threshold: sensor=1, val=25.0, mode=1 (above)
Valeur capteur: 27.5°C
Résultat: 27.5 > 25.0 → LED ON
```

## Compilation et exécution

### Compilation
```bash
mvn clean package
```

### Exécution
```bash
mvn spring-boot:run
```

ou

```bash
java -jar target/arduino-simulator-1.0.0.jar
```

### Tests
```bash
./test-api.sh
```

## Extension possible

### Ajouter un nouveau type de capteur
1. Ajouter dans `SensorType` enum
2. Ajouter la logique dans `Sensor.readValue()`
3. Initialiser dans `SensorService`

### Ajouter un nouveau endpoint
1. Créer la méthode dans le Controller approprié
2. Utiliser les annotations `@GetMapping`, `@PostMapping`, etc.
3. Retourner un `ResponseEntity`

## Dépendances

- **Spring Boot Web**: Framework REST
- **Spring Boot Validation**: Validation des données
- **Lombok**: Réduction du boilerplate (optionnel)

## Notes de développement

- Les valeurs des capteurs sont générées aléatoirement à chaque lecture
- Le threshold n'est pas appliqué automatiquement en continu (pas de scheduler)
- Pas de persistance des données (en mémoire uniquement)
- Pas d'authentification (API publique)
