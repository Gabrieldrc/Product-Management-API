# CHANGELOG

Todos los cambios notables en este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), y este proyecto adhiere
a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned

- **Full English Localization:** Refactorizar todos los mensajes de error del `GlobalExceptionHandler` y los archivos
  JSON de prueba al inglés para mantener consistencia global en el contrato.
- **Dynamic Filtering:** Capacidad de filtrado por categoría y rango de precios.

## [0.5.0] - 2026-01-20

### Added

- **OpenAPI/Swagger Integration:** Implementación completa de `springdoc-openapi`. Configuración programática del bean
  `OpenAPI` con metadatos de contacto y soporte.
- **Advanced Documentation:** Uso de `@Operation`, `@ApiResponses` y `@ExampleObject` en el controlador para mostrar
  múltiples escenarios de error (400, 404, 429) con ejemplos reales tomados de la suite de pruebas.
- **Schema Mapping:** Enriquecimiento de Records (`ProductRequest`, `ErrorResponse`) con anotaciones `@Schema` para
  autogeneración de modelos en la UI de Swagger.
- **UI Optimization:** Implementación de `@ParameterObject` para limpiar la interfaz de parámetros de paginación en
  `GET /products`.

### Changed

- **Error Contract Uniformity:** Refactorización del `GlobalExceptionHandler` para que absolutamente todas las
  excepciones (incluyendo `PropertyReferenceException` y `MethodArgumentTypeMismatchException`) retornen el record
  `ErrorResponse`.
- **Anonymization of Internals:** Ajuste de mensajes de error para ocultar detalles de implementación (como nombres de
  clases Java o Enums) hacia el cliente externo.

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

[0.5.0](https://github.com/Gabrieldrc/example/compare/0.4.0...0.5.0)
[0.4.0](https://github.com/Gabrieldrc/example/compare/0.3.0...0.4.0)
[0.3.0](https://github.com/Gabrieldrc/example/compare/0.2.0...0.3.0)
[0.2.0](https://github.com/Gabrieldrc/example/compare/0.1.0...0.2.0)
[0.1.0](https://github.com/Gabrieldrc/example/releases/tag/v0.1.0)