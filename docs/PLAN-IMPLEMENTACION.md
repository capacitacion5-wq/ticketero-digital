# Plan Detallado de Implementación - Sistema Ticketero

**Proyecto:** Sistema de Gestión de Tickets con Notificaciones en Tiempo Real  
**Versión:** 1.0  
**Fecha:** Diciembre 2025  
**Duración Estimada:** 11 horas  
**Nivel de Complejidad:** Intermedio-Avanzado

---

## 1. Introducción

Este documento proporciona un plan paso a paso para implementar el Sistema Ticketero Digital. El plan está estructurado en 8 fases progresivas que van desde la configuración inicial hasta la implementación de schedulers asíncronos.

**Objetivos:**
- Crear una API REST funcional con Spring Boot 3.2 y Java 21
- Implementar persistencia con PostgreSQL y Flyway
- Aplicar todas las reglas de negocio (RN-001 a RN-013)
- Establecer base para notificaciones Telegram
- Permitir que cualquier desarrollador mid-level siga el plan sin documentación adicional

**Tiempo Total:** ~11 horas de desarrollo

---

## 2. Estructura del Proyecto

```
ticketero/
├── pom.xml
├── .env
├── .gitignore
├── docker-compose.yml
├── Dockerfile
├── README.md
│
├── src/
│   ├── main/
│   │   ├── java/com/example/ticketero/
│   │   │   ├── TicketeroApplication.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── TicketController.java
│   │   │   │   ├── AdminController.java
│   │   │   │   └── HealthController.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── TicketService.java
│   │   │   │   ├── TelegramService.java
│   │   │   │   ├── QueueManagementService.java
│   │   │   │   ├── AdvisorService.java
│   │   │   │   └── NotificationService.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── TicketRepository.java
│   │   │   │   ├── MensajeRepository.java
│   │   │   │   └── AdvisorRepository.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── entity/
│   │   │   │   │   ├── Ticket.java
│   │   │   │   │   ├── Mensaje.java
│   │   │   │   │   └── Advisor.java
│   │   │   │   │
│   │   │   │   ├── dto/
│   │   │   │   │   ├── TicketCreateRequest.java
│   │   │   │   │   ├── TicketResponse.java
│   │   │   │   │   ├── QueuePositionResponse.java
│   │   │   │   │   ├── DashboardResponse.java
│   │   │   │   │   ├── QueueStatusResponse.java
│   │   │   │   │   ├── AdvisorStatsResponse.java
│   │   │   │   │   └── SummaryResponse.java
│   │   │   │   │
│   │   │   │   └── enums/
│   │   │   │       ├── QueueType.java
│   │   │   │       ├── TicketStatus.java
│   │   │   │       ├── AdvisorStatus.java
│   │   │   │       └── MessageTemplate.java
│   │   │   │
│   │   │   ├── scheduler/
│   │   │   │   ├── MensajeScheduler.java
│   │   │   │   └── QueueProcessorScheduler.java
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── RestTemplateConfig.java
│   │   │   │   └── TelegramConfig.java
│   │   │   │
│   │   │   └── exception/
│   │   │       ├── TicketNotFoundException.java
│   │   │       ├── TicketActivoExistenteException.java
│   │   │       ├── AdvisorNotFoundException.java
│   │   │       └── GlobalExceptionHandler.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       └── db/migration/
│   │           ├── V1__create_ticket_table.sql
│   │           ├── V2__create_mensaje_table.sql
│   │           └── V3__create_advisor_table.sql
│   │
│   └── test/
│       └── java/com/example/ticketero/
│           ├── service/
│           │   ├── TicketServiceTest.java
│           │   └── TelegramServiceTest.java
│           └── controller/
│               └── TicketControllerTest.java
│
└── docs/
    ├── PLAN-IMPLEMENTACION.md (este archivo)
    ├── REQUERIMIENTOS-FUNCIONALES.md
    └── ARQUITECTURA.md
```

---

## 3. Configuración Inicial

### 3.1 pom.xml
Ver archivo creado: `pom.xml`

### 3.2 application.yml
Ver archivo creado: `src/main/resources/application.yml`

### 3.3 .env
Ver archivo creado: `.env`

### 3.4 docker-compose.yml
Ver archivo creado: `docker-compose.yml`

### 3.5 Dockerfile
Ver archivo creado: `Dockerfile`

---

## 4. Migraciones de Base de Datos (Flyway)

### 4.1 V1__create_ticket_table.sql
Ver archivo creado: `src/main/resources/db/migration/V1__create_ticket_table.sql`

### 4.2 V2__create_mensaje_table.sql
Ver archivo creado: `src/main/resources/db/migration/V2__create_mensaje_table.sql`

### 4.3 V3__create_advisor_table.sql
Ver archivo creado: `src/main/resources/db/migration/V3__create_advisor_table.sql`

---

## 5. Implementación por Fases

### Fase 0: Setup del Proyecto (30 minutos)

**Objetivo:** Configurar proyecto base y verificar compilación

**Checklist:**
- [x] Crear estructura de carpetas Maven
- [x] Configurar pom.xml con dependencias
- [x] Crear application.yml
- [x] Crear .env
- [x] Crear docker-compose.yml
- [x] Crear Dockerfile

**Verificación:**
```bash
mvn clean compile
```

---

### Fase 1: Migraciones y Enumeraciones (45 minutos)

**Objetivo:** Crear esquema BD y enums Java

**Tareas:**
- [x] V1__create_ticket_table.sql
- [x] V2__create_mensaje_table.sql
- [x] V3__create_advisor_table.sql
- [x] Crear enum QueueType.java
- [x] Crear enum TicketStatus.java
- [x] Crear enum AdvisorStatus.java
- [x] Crear enum MessageTemplate.java

**Criterios de Aceptación:**
- ✅ 3 migraciones SQL creadas
- ✅ 4 enums creados
- ✅ Proyecto compila: `mvn clean compile`

---

### Fase 2: Entities (1 hora)

**Objetivo:** Crear entidades JPA mapeadas a tablas

**Tareas:**
- [x] Crear Ticket.java
- [x] Crear Advisor.java
- [x] Crear Mensaje.java

**Criterios de Aceptación:**
- ✅ 3 entities creadas con anotaciones JPA
- ✅ Relaciones bidireccionales configuradas
- ✅ Proyecto compila: `mvn clean compile`

---

### Fase 3: DTOs (45 minutos)

**Objetivo:** Crear DTOs para request/response

**Tareas:**
- [x] Crear TicketCreateRequest.java
- [x] Crear TicketResponse.java
- [x] Crear QueuePositionResponse.java
- [x] Crear DashboardResponse.java
- [x] Crear QueueStatusResponse.java
- [x] Crear AdvisorStatsResponse.java
- [x] Crear SummaryResponse.java

**Criterios de Aceptación:**
- ✅ 7 DTOs creados
- ✅ Validaciones Bean Validation configuradas
- ✅ Records usados para inmutabilidad

---

### Fase 4: Repositories (30 minutos)

**Objetivo:** Crear interfaces de acceso a datos

**Tareas:**
- [x] Crear TicketRepository.java
- [x] Crear MensajeRepository.java
- [x] Crear AdvisorRepository.java

**Criterios de Aceptación:**
- ✅ 3 repositories creados
- ✅ Queries custom documentadas
- ✅ Proyecto compila

---

### Fase 5: Services (3 horas)

**Objetivo:** Implementar lógica de negocio

**Tareas:**
- [x] Crear TelegramService.java
- [x] Crear AdvisorService.java
- [x] Crear TicketService.java
- [x] Crear QueueManagementService.java
- [x] Crear NotificationService.java

**Criterios de Aceptación:**
- ✅ 5 services implementados
- ✅ Lógica de negocio aplicada
- ✅ Transacciones configuradas

---

### Fase 6: Controllers (2 horas)

**Objetivo:** Exponer API REST

**Tareas:**
- [x] Crear excepciones personalizadas
- [x] Crear GlobalExceptionHandler.java
- [x] Crear TicketController.java
- [x] Crear AdminController.java

**Criterios de Aceptación:**
- ✅ 2 controllers implementados
- ✅ 5+ endpoints funcionales
- ✅ Manejo de errores centralizado
- ✅ Códigos HTTP correctos

---

### Fase 7: Schedulers (1.5 horas)

**Objetivo:** Implementar procesamiento asíncrono

**Tareas:**
- [x] Crear MensajeScheduler.java
- [x] Crear QueueProcessorScheduler.java

**Criterios de Aceptación:**
- ✅ 2 schedulers implementados
- ✅ Procesamiento cada 60s y 5s
- ✅ Manejo de errores en schedulers

---

### Fase 8: Clase Principal y Configuración (30 minutos)

**Objetivo:** Crear clase principal y configuración

**Tareas:**
- [x] Crear TicketeroApplication.java
- [x] Crear RestTemplateConfig.java

**Criterios de Aceptación:**
- ✅ Clase principal con @EnableScheduling
- ✅ RestTemplate configurado
- ✅ Aplicación inicia correctamente

---

## 6. Orden de Ejecución Recomendado

### Día 1 (4 horas)

```
Fase 0: Setup (30 min)
├── Crear estructura Maven
├── Configurar pom.xml
├── Crear application.yml
├── Crear docker-compose.yml
└── Verificar compilación

Fase 1: Migraciones (45 min)
├── Crear 3 archivos SQL
├── Crear 4 enums
└── Verificar migraciones

Fase 2: Entities (1 hora)
├── Crear Ticket.java
├── Crear Advisor.java
├── Crear Mensaje.java
└── Verificar compilación

Fase 3: DTOs (45 min)
├── Crear 7 DTOs
├── Agregar validaciones
└── Verificar compilación

Fase 4: Repositories (30 min)
├── Crear 3 repositories
├── Agregar queries custom
└── Verificar compilación
```

### Día 2 (5 horas)

```
Fase 5: Services (3 horas)
├── TelegramService
├── AdvisorService
├── TicketService
├── QueueManagementService
└── NotificationService

Fase 6: Controllers (2 horas)
├── Excepciones
├── TicketController
└── AdminController
```

### Día 3 (2 horas)

```
Fase 7: Schedulers (1.5 horas)
├── MensajeScheduler
└── QueueProcessorScheduler

Fase 8: Configuración (30 min)
├── TicketeroApplication.java
└── RestTemplateConfig.java
```

---

## 7. Comandos Útiles

### Maven

```bash
# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Empaquetar
mvn clean package -DskipTests

# Ejecutar aplicación
mvn spring-boot:run
```

### Docker

```bash
# Levantar PostgreSQL
docker-compose up -d postgres

# Ver logs
docker-compose logs -f postgres

# Levantar todo
docker-compose up --build

# Detener
docker-compose down -v
```

### PostgreSQL

```bash
# Conectar
docker exec -it ticketero-db psql -U dev -d ticketero

# Ver tablas
\dt

# Ver migraciones
SELECT * FROM flyway_schema_history;

# Ver asesores
SELECT * FROM advisor;
```

### Testing Manual

```bash
# Health check
curl http://localhost:8080/actuator/health

# Crear ticket
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "nationalId": "12345678-9",
    "telefono": "+56912345678",
    "branchOffice": "Sucursal Centro",
    "queueType": "PERSONAL_BANKER"
  }' | jq

# Dashboard
curl http://localhost:8080/api/admin/dashboard | jq
```

---

## 8. Checklist Final de Validación

**Compilación:**
- [ ] `mvn clean compile` sin errores
- [ ] `mvn clean package -DskipTests` genera JAR

**Base de Datos:**
- [ ] PostgreSQL levantado: `docker-compose up -d postgres`
- [ ] 3 migraciones ejecutadas
- [ ] Tabla `flyway_schema_history` con 3 versiones
- [ ] 5 asesores iniciales en tabla `advisor`

**Aplicación:**
- [ ] `mvn spring-boot:run` inicia sin errores
- [ ] Logs muestran: "Started TicketeroApplication"
- [ ] Health endpoint responde: `curl http://localhost:8080/actuator/health`

**API:**
- [ ] POST /api/tickets crea ticket (201)
- [ ] GET /api/tickets/{uuid} obtiene ticket (200)
- [ ] GET /api/admin/dashboard retorna datos (200)
- [ ] Validaciones funcionan (400 con errores)
- [ ] Excepciones manejadas (404, 409)

**Schedulers:**
- [ ] MensajeScheduler ejecuta cada 60s
- [ ] QueueProcessorScheduler ejecuta cada 5s
- [ ] Logs muestran ejecución

---

## 9. Próximos Pasos (PROMPT 4)

Este plan proporciona la estructura base. El PROMPT 4 incluirá:

1. **Implementación Completa de Services**
   - Lógica detallada de asignación
   - Cálculo de posiciones y tiempos
   - Manejo de reintentos

2. **Integración Telegram**
   - Envío real de mensajes
   - Manejo de errores
   - Logging detallado

3. **Tests Unitarios**
   - TicketServiceTest
   - TelegramServiceTest
   - ControllerTests

4. **Documentación API**
   - Swagger/OpenAPI
   - Ejemplos de requests/responses

---

**Documento Completado**  
**Versión:** 1.0  
**Fecha:** Diciembre 2025  
**Estado:** Listo para Implementación
