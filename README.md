Paso 1 — Levantar Kafka con Docker
Abre una terminal en la carpeta kafkaExample/ y ejecuta:
bashdocker-compose up -d
http://localhost:19000
Paso 2 — Correr el Backend Producer
Abre una nueva terminal en kafkaExample/str-producer/ y ejecuta:
.\mvnw spring-boot:run
Paso 3 — Correr el Backend Consumer
Abre una nueva terminal en kafkaExample/str-consumer/ y ejecuta:
.\mvnw spring-boot:run
Paso 4 — Correr el Frontend de Doctores
Abre una nueva terminal en frontProducerKafka/ y ejecuta:
npm install     ← solo la primera vez
npm run dev
Paso 5 — Correr el Frontend de Visualización
Abre una nueva terminal en frontConsumerKafka/ y ejecuta:
npm install     ← solo la primera vez
npm run dev -- --port 5174

Orden de arranque importante
Docker → Producer → Consumer → Frontends

[Frontend Doctores]  →  [Backend Producer]  →  [KAFKA]  →  [Backend Consumer]  →  [Frontend Visualización]
   localhost:5173          localhost:8000       puerto         localhost:8100          localhost:5174
                                                 9092
1. Frontend Doctores (frontProducerKafka - puerto 5173)
Es la interfaz del doctor. Tiene 3 paneles: gestión de pacientes, citas y visualización. Cuando el doctor presiona un botón, hace una llamada HTTP POST al Backend Producer. No sabe nada de Kafka — solo habla con el backend.
2. Backend Producer (str-producer - puerto 8000)
Recibe las peticiones del frontend y las convierte en mensajes de Kafka. Es el único que sabe a qué tópico y en qué partición enviar cada mensaje. Tiene 9 endpoints REST, uno por cada operación.
3. Kafka (puerto 9092)
El corazón del sistema. Almacena los mensajes en 3 tópicos con sus particiones. Los mensajes quedan guardados aunque nadie los esté leyendo en ese momento. El Producer escribe, el Consumer lee — nunca se comunican directo entre sí.
4. Backend Consumer (str-consumer - puerto 8100)
Está constantemente escuchando Kafka. Cuando llega un mensaje a cualquier partición, lo recibe, lo guarda en memoria y lo expone por un endpoint REST. El Frontend Visualización consulta ese endpoint cada 2 segundos.
5. Frontend Visualización (frontConsumerKafka - puerto 5174)
Muestra en tiempo real todos los mensajes que el Consumer ha recibido de Kafka. Permite filtrar por tópico y buscar por ID de paciente.

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
