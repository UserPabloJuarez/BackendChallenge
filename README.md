# Challenge Backend - API REST Spring Boot

## Descripción
API REST desarrollada en Spring Boot (Java 21) que proporciona cálculos con porcentajes dinámicos y registro de historial.

## Funcionalidades
- ✅ Cálculo con porcentaje dinámico desde servicio externo
- ✅ Cache de porcentaje por 30 minutos
- ✅ Historial de llamadas asíncrono
- ✅ Base de datos PostgreSQL
- ✅ Contenedores Docker
- ✅ Documentación Swagger
- ✅ Tests unitarios

## Tecnologías
- Java 21
- Spring Boot 3.2.0
- PostgreSQL
- Docker & Docker Compose
- Swagger/OpenAPI
- JUnit 5 & Mockito

## Instalación y Ejecución

### Prerrequisitos
- Docker
- Docker Compose
- Java 21 (para desarrollo)

### Ejecución con Docker
```bash
# Clonar el proyecto
git clone <repository-url>
cd backend-challenge

# Construir y ejecutar
docker-compose up --build
