¡Claro! Aquí tienes el comando cat para que simplemente lo pegues en tu terminal y se actualice el archivo CHANGELOG.md con la nueva versión 0.4.0 y los pendientes para la 0.5.0.

Bash
cat << 'EOF' > CHANGELOG.md
# CHANGELOG

Todos los cambios notables en este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), y este proyecto adhiere
a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Integración de OpenAPI/Swagger para documentación de contratos (v0.5.0).
- Capacidad de filtrado dinámico por condición y título en `GET /products` (En planificación).

## [0.4.0] - 2026-01-19

### Added
- **Aislamiento de Base de Datos:** Implementación de `resetDatabaseState()` con SQL nativo (`TRUNCATE RESTART IDENTITY`) para garantizar que cada archivo de prueba dinámico inicie con IDs limpios.
- **Resiliencia Multi-Entorno:** Soporte en el motor de tests para nombres de tablas tanto en plural (`products`) como en singular (`product`), evitando fallos por estrategias de nombrado de Hibernate.
- **Consistencia de Errores:** Alineación del formato JSON de error del `RateLimitingFilter` con el `GlobalExceptionHandler`, incluyendo el campo `timestamp`.

### Changed
- **Arquitectura de Filtros:** Refactorización del `RateLimitingFilter` para utilizar un formato de respuesta manual pero idéntico al DTO `ErrorResponse`, asegurando que las respuestas 429 sigan el estándar de la API.
- **Optimización de Tests:** Inyección directa de `EntityManager` en el motor dinámico para mejorar el rendimiento de la limpieza de tablas.

### Fixed
- **SQLGrammarException:** Corregido el error de "Table PRODUCT not found" que bloqueaba la ejecución de tests dinámicos en entornos H2 estrictos.
- **ID Mismatch:** Eliminada la deriva de IDs entre ejecuciones de archivos JSON mediante el reinicio forzado de secuencias de identidad.

## [0.3.0] - 2026-01-19

### Added
- **Paginación Profesional:** Implementación de `PaginatedResponse<T>` (DTO) para limpiar la respuesta de Spring Data y evitar la exposición de metadatos internos.
- **Control de Entorno:** Añadido `@ActiveProfiles("test")` al motor dinámico para garantizar la independencia de los IDs y desactivar el seeding en pruebas.
- **Robustez:** Caso de prueba `http02_errors.json` que valida la captura de errores 400 mediante Bean Validation y `GlobalExceptionHandler`.

### Changed
- **Contratos de API:** El endpoint `GET /products` ahora retorna un objeto JSON estructurado con `content`, `totalElements`, `totalPages`, `pageNumber` y `pageSize`.
- **Refactor de Seeder:** Actualizada la lógica de `ProductDataSeeder` para trabajar con el nuevo DTO paginado.

### Fixed
- **Idempotencia de IDs:** Solucionado el problema de desajuste de IDs en los tests dinámicos mediante el aislamiento de perfiles.

## [0.2.0] - 2026-01-19

### Added
- Dependencia de Bucket4j en el pom.xml para el soporte de Rate Limiting.
- Suite de Tests Unitarios completos para `ProductServiceImpl` cubriendo los 18 casos de prueba de QA.
- Implementación de Data Seeding mediante `ProductDataSeeder` para entornos de desarrollo.
- Análisis de estrategia híbrida de testing: Motor Dinámico para regresión y Tests Convencionales para seguridad.

### Changed
- Refactorización de `ModelController` a `ProductController` con inyección por constructor.
- Migración de lógica a `ProductServiceImpl` con soporte para Java Records (DTOs).
- Mejora de seguridad de tipos sustituyendo `Model` por `Product`.
- Migración completa del motor de tests dinámicos de JUnit 4 a JUnit 5.
- Sustitución de `javafx.util.Pair` por Java Records internos en el motor de tests.

### Fixed
- **Estabilidad de Tests:** Añadido `@DirtiesContext` para evitar colisiones por Rate Limit.
- **Corrección de Motor:** Ajustada la firma de `addTestFailure` para el sistema de reportes.
- **Robustez de Datos:** Añadido `productRepository.deleteAll()` en el `@BeforeEach`.

## [0.1.0] - 2026-01-19

### Added
- Configuración inicial del proyecto con Java 21 y Spring Boot 3.2.5.
- Modelo de persistencia `Product` con soporte para JPA.
- Estructura de DTOs utilizando Java Records (`ProductRequest` y `ProductResponse`).
- `GlobalExceptionHandler` centralizado para gestión de errores 404 y 400.

[0.4.0](https://github.com/Gabrieldrc/example/compare/0.3.0...0.4.0)
[0.3.0](https://github.com/Gabrieldrc/example/compare/0.2.0...0.3.0)
[0.2.0](https://github.com/Gabrieldrc/example/compare/0.1.0...0.2.0)
[0.1.0](https://github.com/Gabrieldrc/example/releases/tag/v0.1.0)