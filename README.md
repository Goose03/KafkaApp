# 🏥 KafkaApp — Sistema Hospitalario con Apache Kafka

Sistema distribuido de gestión hospitalaria que utiliza **Apache Kafka** como broker de mensajes para comunicar sus componentes de forma asíncrona y desacoplada.

---

## 🗂️ Estructura del proyecto

```
KafkaApp/
├── kafkaExample/
│   ├── docker-compose.yml      ← Kafka + Zookeeper + Kafdrop
│   ├── str-producer/           ← Backend Producer  (puerto 8000)
│   └── str-consumer/           ← Backend Consumer  (puerto 8100)
├── frontProducerKafka/         ← Frontend Doctores (puerto 5173)
└── frontConsumerKafka/         ← Frontend Visualización (puerto 5174)
```

---

## ⚙️ Requisitos previos

- **Docker Desktop**
- **Java 17+**
- **Node.js**

---

## 🚀 Cómo correr el proyecto

> ⚠️ **Orden de arranque importante:** `Docker → Producer → Consumer → Frontends`

### Paso 1 — Levantar Kafka

Abre una terminal en `kafkaExample/` y ejecuta:

```bash
docker-compose up -d
```

Verifica que Kafka esté corriendo en:
```
http://localhost:19000
```

---

### Paso 2 — Correr el Backend Producer

Abre una nueva terminal en `kafkaExample/str-producer/`:

```bash
# Windows
.\mvnw spring-boot:run

# Mac / Linux
./mvnw spring-boot:run
```

> ✅ El Producer crea automáticamente los 3 tópicos en Kafka al arrancar.

---

### Paso 3 — Correr el Backend Consumer

Abre una nueva terminal en `kafkaExample/str-consumer/`:

```bash
# Windows
.\mvnw spring-boot:run

# Mac / Linux
./mvnw spring-boot:run
```

---

### Paso 4 — Correr el Frontend de Doctores

Abre una nueva terminal en `frontProducerKafka/`:

```bash
npm install       # solo la primera vez
npm run dev
```

Disponible en: `http://localhost:5173`

---

### Paso 5 — Correr el Frontend de Visualización

Abre una nueva terminal en `frontConsumerKafka/`:

```bash
npm install       # solo la primera vez
npm run dev -- --port 5174
```

Disponible en: `http://localhost:5174`

---

## 🌐 Puertos del sistema

| Servicio | Puerto | URL |
|---|---|---|
| Kafka | 9092 | — |
| Kafdrop (UI de Kafka) | 19000 | http://localhost:19000 |
| Backend Producer | 8000 | http://localhost:8000 |
| Backend Consumer | 8100 | http://localhost:8100/consumer/mensajes |
| Frontend Doctores | 5173 | http://localhost:5173 |
| Frontend Visualización | 5174 | http://localhost:5174 |

---

## 🏗️ Arquitectura del sistema

```
[Frontend Doctores]        [Backend Producer]        [KAFKA]        [Backend Consumer]        [Frontend Visualización]
  localhost:5173      →      localhost:8000      →   puerto 9092  →   localhost:8100      →        localhost:5174
```

### Componentes

**1. Frontend Doctores** — `frontProducerKafka` — puerto 5173

Interfaz del doctor con 3 paneles: gestión de pacientes, citas y visualización. Cuando el doctor presiona un botón, hace una llamada HTTP POST al Backend Producer. No sabe nada de Kafka — solo habla con el backend.

**2. Backend Producer** — `str-producer` — puerto 8000

Recibe las peticiones del frontend y las convierte en mensajes de Kafka. Es el único que sabe a qué tópico y en qué partición enviar cada mensaje. Tiene 9 endpoints REST, uno por cada operación.

**3. Kafka** — puerto 9092

El corazón del sistema. Almacena los mensajes en 3 tópicos con sus particiones. Los mensajes quedan guardados aunque nadie los esté leyendo en ese momento. El Producer escribe, el Consumer lee — nunca se comunican directo entre sí.

**4. Backend Consumer** — `str-consumer` — puerto 8100

Está constantemente escuchando Kafka. Cuando llega un mensaje a cualquier partición, lo recibe, lo guarda en memoria y lo expone por un endpoint REST. El Frontend Visualización consulta ese endpoint cada 2 segundos.

**5. Frontend Visualización** — `frontConsumerKafka` — puerto 5174

Muestra en tiempo real todos los mensajes que el Consumer ha recibido de Kafka. Permite filtrar por tópico y buscar por ID de paciente.

---

## 📨 Tópicos y Particiones

```
TÓPICO: gestion-pacientes (3 particiones)
├── Partición 0 → Registro de paciente nuevo
├── Partición 1 → Actualización de datos
└── Partición 2 → Eliminación de registro

TÓPICO: gestion-citas (3 particiones)
├── Partición 0 → Creación de cita
├── Partición 1 → Cancelación de cita
└── Partición 2 → Reprogramación de cita

TÓPICO: visualizacion-estado (2 particiones)
├── Partición 0 → Consulta de estado del paciente
└── Partición 1 → Consulta de historial médico
```

---

## 🔄 Flujo de un mensaje

```
1. Doctor presiona "Registrar paciente" en localhost:5173
2. Frontend hace POST → http://localhost:8000/producer/pacientes/registro
3. Producer publica el mensaje en Kafka:
   tópico: gestion-pacientes | partición: 0
4. Kafka almacena el mensaje
5. Consumer detecta el mensaje y lo guarda en memoria
6. Frontend Visualización consulta GET → http://localhost:8100/consumer/mensajes
7. El mensaje aparece en localhost:5174 en menos de 2 segundos
```

---

## 🛑 Para detener el proyecto

```bash
# Detener frontends y backends → Ctrl+C en cada terminal

# Detener Kafka (conserva los datos)
docker-compose down

# Detener Kafka y borrar todos los datos
docker-compose down -v
```

> Usa `docker-compose down -v` si quieres reiniciar desde cero con los tópicos limpios.
