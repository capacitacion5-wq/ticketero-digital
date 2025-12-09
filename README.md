# Ticketero Digital - Sistema de Gestión de Tickets

Sistema digital de gestión de tickets para atención en sucursales de instituciones financieras con notificaciones en tiempo real vía Telegram.

## 📋 Descripción

Moderniza la experiencia de atención presencial mediante:
- Digitalización del proceso de tickets
- Notificaciones automáticas en tiempo real vía Telegram
- Movilidad del cliente durante la espera
- Asignación automática de clientes a ejecutivos disponibles
- Panel de monitoreo para supervisión operacional

## 🏗️ Arquitectura

- **Backend**: Java 21 + Spring Boot 3.2
- **Base de Datos**: PostgreSQL
- **Infraestructura**: AWS (ECS, RDS, ElastiCache)
- **Notificaciones**: Telegram Bot API

## 📁 Estructura del Proyecto

```
proyecto1/
├── docs/
│   ├── project-requirements.md      # Requerimientos del proyecto
│   ├── PROJECT-PROPOSE.md           # Propuesta arquitectónica
│   └── architecture-diagram.md      # Diagrama de arquitectura AWS
├── src/                             # Código fuente (por implementar)
└── README.md                        # Este archivo
```

## 🚀 Roadmap

- **Sprint 1-2**: Core Domain (Entidades, Repositorios, Casos de Uso)
- **Sprint 3-4**: Notificaciones (Integración Telegram, Retry)
- **Sprint 5-6**: Dashboard (WebSocket, Métricas)
- **Sprint 7-8**: Optimización y Deploy

## 📊 Fases de Implementación

1. **Fase Piloto**: 1 sucursal, 500-800 tickets/día
2. **Fase Expansión**: 5 sucursales, 2,500-3,000 tickets/día
3. **Fase Nacional**: 50+ sucursales, 25,000+ tickets/día

## 📝 Documentación

Ver carpeta `/docs` para:
- Requerimientos funcionales y no funcionales
- Propuesta arquitectónica detallada
- Diagrama de arquitectura AWS

## 👨‍💻 Desarrollo

```bash
# Clonar repositorio
git clone https://github.com/tu_usuario/ticketero-digital.git

# Navegar al proyecto
cd ticketero-digital

# Configurar variables de entorno
cp .env.example .env

# Iniciar desarrollo
# (Instrucciones específicas por implementar)
```

## 📄 Licencia

Proyecto de capacitación - Ciclo Completo de Desarrollo de Software

---

**Preparado por**: Área de Producto e Innovación  
**Versión**: 1.0  
**Fecha**: Diciembre 2025
