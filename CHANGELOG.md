# CHANGELOG

Todos los cambios notables en este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), y este proyecto adhiere
a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned

- **Dynamic Filtering:** Capacidad de filtrado por categoría y rango de precios.
- **Dockerization:** Creación de imagen optimizada para despliegue en contenedores.

## [0.7.0] - 2026-01-20

### Added

- **Spring Boot Actuator Integration:** Implementación de endpoints de observabilidad (`/health`, `/metrics`, `/info`)
  para monitoreo del estado del servicio.
- **Single Source of Truth (Version sync):** Configuración de Maven Resource Filtering para sincronizar automáticamente
  la versión del `pom.xml` con los endpoints de Actuator y la documentación de Swagger.
- **Health Indicators:** Activación de detalles de salud para verificar la conectividad de la base de datos en tiempo
  real.

### Changed

- **Maven Build Lifecycle:** Actualización del `spring-boot-maven-plugin` para generar información de compilación (
  `build-info`) automáticamente.
- **Project Configuration:** Migración de valores estáticos en `application.properties` a variables dinámicas de Maven (
  @project.version@).

## [0.6.0] - 2026-01-20

### Added

- **Full English Localization:** Refactorización total de mensajes de error en `GlobalExceptionHandler`, DTOs y modelos
  al inglés técnico.
- **Global Rate Limit Visibility:** Documentación del error 429 (Too Many Requests) en todos los endpoints del
  controlador.

### Fixed

- **Ghost Documentation Fix:** Desactivada la inferencia automática de respuestas de error mediante
  `springdoc.override-with-generic-response=false`, eliminando respuestas 404 erróneas en endpoints de creación y
  listado.
- **Test Consistency:** Actualización de la suite de pruebas JSON para alinearse con los nuevos mensajes de error en
  inglés.

## [0.5.0] - 2026-01-20

### Added

- **OpenAPI/Swagger Integration:** Implementación de `springdoc-openapi` con metadatos profesionales.
- **Advanced Documentation:** Uso de `@Operation` y `@ExampleObject` para mostrar múltiples escenarios de error con
  ejemplos reales.
- **UI Optimization:** Implementación de `@ParameterObject` para una interfaz de paginación limpia.

## [0.4.0] - 2026-01-19

### Added

- **Aislamiento de Base de Datos:** Implementación de `resetDatabaseState()` con SQL nativo (
  `TRUNCATE RESTART IDENTITY`) para garantizar IDs limpios.
- **Resiliencia Multi-Entorno:** Soporte para nombres de tablas en plural y singular.
- **Consistencia de Errores:** Alineación del formato JSON de error del `RateLimitingFilter` con el
  `GlobalExceptionHandler`.

### Changed

- **Arquitectura de Filtros:** Refactorización del `RateLimitingFilter` para seguir el estándar del DTO `ErrorResponse`.
- **Optimización de Tests:** Inyección directa de `EntityManager` para limpieza de tablas.

### Fixed

- **SQLGrammarException:** Corregido el error de "Table PRODUCT not found" en entornos H2 estrictos.
- **ID Mismatch:** Eliminada la deriva de IDs mediante reinicio forzado de secuencias.

## [0.3.0] - 2026-01-19

### Added

- **Paginación Profesional:** Implementación de `PaginatedResponse<T>` (DTO) para evitar la exposición de metadatos
  internos de Spring Data.
- **Control de Entorno:** Añadido `@ActiveProfiles("test")` al motor dinámico.
- **Robustez:** Caso de prueba `http02_errors.json` para validación de Bean Validation.

## [0.2.0] - 2026-01-19

### Added

- Dependencia de Bucket4j para Rate Limiting.
- Suite de Tests Unitarios completos (18 casos de prueba).
- Implementación de `ProductDataSeeder`.

## [0.1.0] - 2026-01-19

### Added

- Configuración inicial con Java 21 y Spring Boot 3.2.5.
- Modelo `Product` y estructura inicial de DTOs.
- `GlobalExceptionHandler` centralizado.

[unreleased]: https://github.com/Gabrieldrc/example/compare/0.7.0...HEAD

[0.7.0]: https://github.com/Gabrieldrc/example/compare/0.6.0...0.7.0

[0.6.0]: https://github.com/Gabrieldrc/example/compare/0.5.0...0.6.0

[0.5.0]: https://github.com/Gabrieldrc/example/compare/0.4.0...0.5.0

[0.4.0]: https://github.com/Gabrieldrc/example/compare/0.3.0...0.4.0

[0.3.0]: https://github.com/Gabrieldrc/example/compare/0.2.0...0.3.0

[0.2.0]: https://github.com/Gabrieldrc/example/compare/0.1.0...0.2.0

[0.1.0]: https://github.com/Gabrieldrc/example/releases/tag/v0.1.0