# Arduino Simulator - API REST

Simulateur d'un système Arduino avec capteurs (Température et Luminosité) et LED contrôlable via API REST.

## 🚀 Démarrage

### Prérequis
- Java 17 ou supérieur
- Maven 3.6+

### Lancement
```bash
mvn spring-boot:run
```

L'API sera accessible sur `http://localhost:8080`

## 📡 Endpoints disponibles

### LED

#### Allumer la LED
```bash
POST /led/on
```
Réponse: `"Led allumée"`

#### Éteindre la LED
```bash
POST /led/off
```
Réponse: `"Led éteinte"`

#### Toggle la LED
```bash
POST /led/toggle
```
Réponse: 
```json
{
  "status": 1
}
```

#### Obtenir l'état de la LED
```bash
GET /led
```
Réponse:
```json
{
  "status": 1,
  "pos": 13
}
```

### Threshold (Seuil)

#### Définir un seuil
```bash
POST /led/threshold
Content-Type: application/json

{
  "sensor": 1,
  "val": 25.0,
  "mode": 1
}
```
- `sensor`: ID du capteur
- `val`: Valeur du seuil
- `mode`: 0 = below (en dessous), 1 = above (au dessus)

Réponse: `"Threshold modifié"`

#### Obtenir le seuil
```bash
GET /led/threshold
```
Réponse:
```json
{
  "sensor": 1,
  "val": 25.0,
  "mode": 1
}
```

#### Supprimer le seuil
```bash
DELETE /led/threshold
```
Réponse: `"Threshold supprimé"`

### Capteurs

#### Obtenir tous les capteurs
```bash
GET /sensor
```
Réponse:
```json
{
  "sensors": [
    {
      "id": 1,
      "name": "Temperature Sensor 1",
      "val": 23.45,
      "unit": "°C",
      "pos": 1
    },
    {
      "id": 2,
      "name": "Light Sensor 1",
      "val": 567.89,
      "unit": "lux",
      "pos": 2
    }
  ]
}
```

#### Obtenir un capteur spécifique
```bash
GET /sensor?id=1
```

#### Obtenir plusieurs capteurs
```bash
GET /sensor?ids=1,2
```

### Status (Endpoint combiné)

```bash
GET /status?led=1&sensor_ids=1,2&threshold=1
```
Réponse:
```json
{
  "leds": [
    {
      "status": 1,
      "pos": 13
    }
  ],
  "sensors": [
    {
      "id": 1,
      "name": "Temperature Sensor 1",
      "val": 23.45,
      "unit": "°C",
      "pos": 1
    }
  ],
  "thresholds": [
    {
      "sensor": 1,
      "val": 25.0,
      "mode": 1
    }
  ]
}
```

## 🔧 Configuration

### Capteurs disponibles
- **Capteur 1**: Température (15-30°C)
- **Capteur 2**: Luminosité (0-1000 lux)
- **Capteur 3**: Température (15-30°C)

Les valeurs sont générées aléatoirement pour simuler des lectures réelles.

## 📝 Exemples d'utilisation

### Exemple complet avec curl

```bash
# Allumer la LED
curl -X POST http://localhost:8080/led/on

# Obtenir les informations de tous les capteurs
curl http://localhost:8080/sensor

# Définir un seuil: allumer la LED si température > 25°C
curl -X POST http://localhost:8080/led/threshold \
  -H "Content-Type: application/json" \
  -d '{"sensor": 1, "val": 25.0, "mode": 1}'

# Obtenir le status complet
curl "http://localhost:8080/status?led=1&sensor_ids=1,2&threshold=1"
```

## 🏗️ Structure du projet

```
com.arduino.simulator/
├── controller/
│   ├── LedController.java
│   ├── SensorController.java
│   └── StatusController.java
├── service/
│   ├── LedService.java
│   └── SensorService.java
├── model/
│   ├── Sensor.java
│   ├── SensorType.java
│   └── Threshold.java
└── dto/
    ├── LedResponse.java
    ├── SensorResponse.java
    ├── ThresholdRequest.java
    └── ThresholdResponse.java
```

## 🎯 Fonctionnalités

- ✅ Gestion de LED (on/off/toggle)
- ✅ Lecture de capteurs (température et luminosité)
- ✅ Système de seuils automatiques
- ✅ Valeurs de capteurs simulées de façon réaliste
- ✅ API REST conforme au code Arduino original
