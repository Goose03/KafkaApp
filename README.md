# 🏥 KafkaApp — Sistema Hospitalario con Apache Kafka

Sistema distribuido de gestión hospitalaria que utiliza **Apache Kafka** como broker de mensajes para comunicar sus componentes de forma asíncrona y desacoplada.

<img width="1513" height="784" alt="3d366513-0b35-4975-9638-6d189468a7b4" src="https://github.com/user-attachments/assets/8a0ce307-f0b0-4da2-a946-d7a4564d3287" />

## ✅ Demostración — El sistema en funcionamiento

### Sistema para Doctores (`localhost:5173`)
Interfaz con 3 paneles activos. El doctor puede registrar pacientes, gestionar citas y consultar estados. Cada botón envía el mensaje al tópico y partición correcta en Kafka.

> Ejemplo real: Se registró a **JUAN (ID: 001)** y **PABLO (ID: 002)**, se eliminó a PABLO, y se actualizó su información — cada operación fue a una partición distinta del tópico `gestion-pacientes`.
<img width="1512" height="790" alt="58af827d-35f7-4bbc-84a6-7203706de72a" src="https://github.com/user-attachments/assets/df279b74-adcc-4c83-b9bf-433a36000d3a" />
<img width="581" height="365" alt="b3a30732-a872-41ae-a932-b4b77a119f3f" src="https://github.com/user-attachments/assets/01cfd44d-8fd4-4aa5-930f-b353f38e600b" />


### Sistema de Visualización (`localhost:5174`)
<img width="1512" height="791" alt="306939ab-75fe-41b0-9c1b-a32b7ba4098b" src="https://github.com/user-attachments/assets/909cebd2-aeac-475f-a1e1-a2d120eb469d" />
<img width="604" height="297" alt="image" src="https://github.com/user-attachments/assets/bcd9c790-ea12-445d-9ef0-7331f3d0aba0" />

**Tópico: Gestión de Pacientes** — los 4 eventos llegan correctamente a sus particiones:
```
Gestión de Pacientes  |  Partición 0 — Registro    →  "Registrar paciente: JUAN, ID: 001"
Gestión de Pacientes  |  Partición 0 — Registro    →  "Registrar paciente: PABLO, ID: 002"
Gestión de Pacientes  |  Partición 2 — Eliminación →  "Eliminar paciente ID: 002"
Gestión de Pacientes  |  Partición 1 — Actualización → "Actualizar paciente ID: 002, nombre: PABLO GIL"
```

**Tópico: Gestión de Citas** — creación, reprogramación y cancelación en sus particiones:
```
Gestión de Citas  |  Partición 0 — Creación       →  "Crear cita para paciente ID: 001, fecha: 2026-04-15"
Gestión de Citas  |  Partición 0 — Creación       →  "Crear cita para paciente ID: 002, fecha: 2026-04-17"
Gestión de Citas  |  Partición 2 — Reprogramación →  "Reprogramar cita de paciente ID: 002, nueva fecha: 2026-04-17"
Gestión de Citas  |  Partición 1 — Cancelación    →  "Cancelar cita de paciente ID: 001"
```

**Tópico: Visualización / Estado** — consultas de estado e historial médico:
```
Visualización / Estado  |  Partición 0 — Consulta Estado  →  "Consultar estado del paciente ID: 001"
Visualización / Estado  |  Partición 1 — Historial Médico →  "Consultar historial médico del paciente ID: 001"
```

---

### Buscador funcional
Al escribir `001` en el buscador, el sistema filtra y muestra únicamente los mensajes relacionados con ese paciente a través de los 3 tópicos.

---

### Verificación en Kafdrop (`localhost:19000`)
Los mensajes se pueden verificar directamente en Kafka. Kafdrop muestra cada mensaje con su **partición**, **offset** y **timestamp** exactos:

**Tópico `gestion-pacientes`:**
```
Partition: 0  Offset: 0  →  "Registrar paciente: JUAN, ID: 001"
Partition: 0  Offset: 1  →  "Registrar paciente: PABLO, ID: 002"
Partition: 2  Offset: 0  →  "Eliminar paciente ID: 002"
Partition: 1  Offset: 0  →  "Actualizar paciente ID: 002, nombre: PABLO GIL"
```

**Tópico `visualizacion-estado`:**
```
Partition: 1  Offset: 0  →  "Consultar historial médico del paciente ID: 001"
```

Esto confirma que el Producer enruta cada mensaje a la partición correcta según el tipo de operación, y el Consumer los recibe y los expone al Frontend de Visualización en tiempo real.

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
