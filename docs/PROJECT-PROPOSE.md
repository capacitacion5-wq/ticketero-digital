# **Propuesta Arquitectónica - Sistema de Gestión de Tickets**

**Proyecto:** Ticketero Digital con Notificaciones en Tiempo Real  
**Arquitecto:** Sistema Senior Java/Spring Boot  
**Stack Tecnológico:** Java 21 + Spring Boot 3.2 + PostgreSQL  
**Enfoque:** Pragmático y Evolutivo  
**Fecha:** Diciembre 2025

---

## **1. Análisis del Dominio y Módulos Principales**

### **Módulos Core del Sistema:**
- **Ticket Management**: Creación, estado y ciclo de vida de tickets
- **Queue Management**: Gestión de colas por tipo de atención
- **Executive Management**: Asignación automática y balanceo de carga
- **Notification Service**: Integración con Telegram y lógica de mensajería
- **Monitoring Dashboard**: Panel en tiempo real para supervisores
- **Audit Service**: Trazabilidad completa de eventos

**¿Por qué estos módulos?** Cada uno representa un bounded context claro con responsabilidades específicas, evitando acoplamiento innecesario.

## **2. Arquitectura Recomendada: Monolito Modular**

### **Justificación:**
- **Escala inicial**: 500-800 tickets/día no justifica microservicios
- **Complejidad operacional**: Una sola aplicación es más fácil de desplegar y monitorear
- **Transacciones**: Las operaciones de ticket-cola-ejecutivo requieren consistencia
- **Evolución**: Estructura modular permite extraer servicios cuando sea necesario

### **Patrón de Arquitectura:**
```
Hexagonal Architecture (Ports & Adapters)
├── Domain Layer (Entities, Value Objects, Domain Services)
├── Application Layer (Use Cases, Command/Query Handlers)
├── Infrastructure Layer (Repositories, External Services)
└── Presentation Layer (REST Controllers, WebSocket)
```

## **3. Estructura del Proyecto Spring Boot 3.2**

```
src/main/java/com/financiera/ticketero/
├── domain/
│   ├── ticket/        # Agregado Ticket
│   ├── queue/         # Agregado Queue  
│   ├── executive/     # Agregado Executive
│   └── shared/        # Value Objects compartidos
├── application/
│   ├── ticket/        # Use Cases de tickets
│   ├── queue/         # Use Cases de colas
│   ├── notification/  # Use Cases de notificaciones
│   └── monitoring/    # Use Cases de dashboard
├── infrastructure/
│   ├── persistence/   # JPA Repositories
│   ├── messaging/     # Telegram Integration
│   ├── websocket/     # Real-time updates
│   └── config/        # Configuraciones
└── presentation/
    ├── rest/          # REST Controllers
    └── websocket/     # WebSocket Controllers
```

## **4. Diseño del Dominio**

### **Agregados Principales:**

**Ticket Aggregate:**
```java
@Entity
public class Ticket {
    private TicketId id;
    private CustomerId customerId;
    private QueueType queueType;
    private TicketStatus status;
    private LocalDateTime createdAt;
    private ExecutiveId assignedExecutive;
    private Integer position;
    private Duration estimatedWaitTime;
}
```

**Queue Aggregate:**
```java
@Entity
public class Queue {
    private QueueId id;
    private QueueType type;
    private List<TicketId> waitingTickets;
    private QueueConfiguration config; // tiempo promedio, prioridad
}
```

**Executive Aggregate:**
```java
@Entity
public class Executive {
    private ExecutiveId id;
    private ExecutiveName name;
    private Set<QueueType> supportedQueues;
    private ExecutiveStatus status; // AVAILABLE, BUSY, OFFLINE
    private TicketId currentTicket;
}
```

### **DTOs Específicos:**
- `CreateTicketRequest/Response`
- `TicketStatusResponse`
- `DashboardMetricsResponse`
- `NotificationMessage`

## **5. Estrategia de Persistencia PostgreSQL**

### **Esquema de Base de Datos:**
```sql
-- Tablas principales
tickets (id, customer_id, queue_type, status, created_at, assigned_executive_id, position, estimated_wait_minutes)
queues (id, type, average_service_time_minutes, priority_level)
executives (id, name, status, current_ticket_id)
queue_executives (queue_id, executive_id) -- Many-to-many
audit_events (id, event_type, entity_id, timestamp, actor, changes)

-- Índices críticos para performance
CREATE INDEX idx_tickets_status_queue ON tickets(status, queue_type);
CREATE INDEX idx_tickets_created_at ON tickets(created_at);
CREATE INDEX idx_executives_status ON executives(status);
```

### **Migraciones con Flyway:**
- **¿Por qué Flyway?** Más simple que Liquibase, perfecto para este proyecto
- Versionado: `V1__create_initial_schema.sql`
- Scripts idempotentes para rollback seguro

### **Estrategia de Datos:**
- **JPA con Hibernate**: Para operaciones CRUD estándar
- **Native Queries**: Para cálculos complejos de posición y tiempo estimado
- **Connection Pooling**: HikariCP (incluido en Spring Boot)

## **6. Integraciones Externas**

### **Telegram Bot API:**
```java
@Component
public class TelegramNotificationService {
    // Implementación con RestTemplate/WebClient
    // Retry automático con @Retryable
    // Circuit breaker con Resilience4j
}
```

### **WebSocket para Dashboard:**
```java
@Controller
public class DashboardWebSocketController {
    // Actualizaciones cada 5 segundos
    // Broadcast de métricas en tiempo real
}
```

## **7. Buenas Prácticas Java 21 + Spring Boot 3.2**

### **Características Java 21:**
- **Virtual Threads**: Para manejar conexiones WebSocket concurrentes
- **Pattern Matching**: En switch expressions para estados de ticket
- **Records**: Para DTOs inmutables

### **Spring Boot 3.2:**
- **Native Compilation**: Para startup más rápido (opcional)
- **Observability**: Micrometer + Prometheus para métricas
- **Configuration Properties**: `@ConfigurationProperties` para configuración tipada

### **Configuración de Aplicación:**
```yaml
app:
  telegram:
    bot-token: ${TELEGRAM_BOT_TOKEN}
    retry-attempts: 3
  queue:
    update-interval: 5s
  database:
    pool-size: 10
```

## **8. Deployment y Herramientas**

### **Containerización:**
```dockerfile
FROM eclipse-temurin:21-jre-alpine
# Multi-stage build para optimizar tamaño
# Health checks incluidos
```

### **Orquestación (si aporta valor):**
- **Docker Compose**: Para desarrollo local
- **Kubernetes**: Solo si hay múltiples sucursales (Fase Nacional)
- **Alternativa simple**: Deployment directo con systemd

### **IaC Recomendado:**
- **Terraform**: Para infraestructura AWS (RDS, ECS/EC2)
- **Ansible**: Para configuración de servidores (si no se usa contenedores)

### **Monitoreo:**
- **Spring Boot Actuator**: Health checks y métricas
- **Prometheus + Grafana**: Métricas de negocio
- **Structured Logging**: JSON con Logback

## **9. Consideraciones de Escalabilidad**

### **Fase Piloto (1 sucursal):**
- Single instance con PostgreSQL
- Telegram webhook directo

### **Fase Expansión (5 sucursales):**
- Load balancer + múltiples instancias
- Redis para sesiones WebSocket
- PostgreSQL con read replicas

### **Fase Nacional (50+ sucursales):**
- Considerar extracción de Notification Service
- Event-driven architecture con Apache Kafka
- Sharding por sucursal si es necesario

## **10. Puntos Críticos de Implementación**

### **Cálculo de Tiempo Estimado:**
```java
public Duration calculateEstimatedWaitTime(QueueType queueType, int position) {
    // Algoritmo basado en: posición * tiempo_promedio / ejecutivos_disponibles
    // Considerar histórico de últimas 2 horas
}
```

### **Asignación Automática:**
- Event-driven con Spring Events
- Algoritmo round-robin con prioridades
- Fallback manual si falla automatización

### **Resiliencia en Notificaciones:**
- Retry exponencial con jitter
- Dead letter queue para mensajes fallidos
- Monitoring de tasa de entrega

## **11. Roadmap de Implementación**

### **Sprint 1-2: Core Domain**
- Entidades principales y repositorios
- Casos de uso básicos (crear ticket, asignar ejecutivo)
- Base de datos y migraciones

### **Sprint 3-4: Notificaciones**
- Integración con Telegram
- Sistema de retry y resilencia
- Eventos de dominio

### **Sprint 5-6: Dashboard y Monitoreo**
- WebSocket para tiempo real
- Métricas y alertas
- Panel de supervisión

### **Sprint 7-8: Optimización y Deploy**
- Performance tuning
- Containerización
- Deployment en producción

## **12. Métricas de Éxito Técnico**

### **Performance:**
- Creación de ticket < 3 segundos
- Cálculo de posición < 1 segundo
- Latencia de notificaciones < 5 segundos

### **Confiabilidad:**
- Uptime > 99.5%
- Tasa de entrega de mensajes > 99.9%
- Recovery time < 5 minutos

### **Escalabilidad:**
- Soporte para 800 tickets/día (Fase Piloto)
- Preparado para 25,000+ tickets/día (Fase Nacional)

---

**Conclusión:** Esta arquitectura prioriza simplicidad operacional manteniendo flexibilidad para evolucionar. El monolito modular permite entregar valor rápidamente mientras se construye conocimiento del dominio para futuras decisiones arquitectónicas.

**Próximos Pasos:**
1. Validar propuesta con stakeholders técnicos
2. Definir entorno de desarrollo
3. Configurar pipeline CI/CD
4. Iniciar implementación del core domain