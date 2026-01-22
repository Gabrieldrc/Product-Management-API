# CHANGELOG

Todos los cambios notables en este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), y este proyecto adhiere
a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.0] - 2026-01-22

### Added

- **Comprehensive API Documentation:** Se agregó y actualizó el archivo `README.md` para incluir una descripción detallada de la API, instrucciones de configuración y ejecución, guía de autenticación, endpoints de monitoreo, documentación interactiva de Swagger UI, y decisiones arquitectónicas clave.

## [0.11.0] - 2026-01-22

### Added

- **Item Detail API Expansion:** Ampliación del modelo `Product` y DTOs para incluir `description`, `seller_info` (name,
  rating) y `shipping_info` (cost, delivery).
- **SQL Data Seed Migration:** Implementación de `data.sql` para la carga inicial de datos persistentes, sustituyendo el
  antiguo `ProductDataSeeder`.
- **Advanced JSON Assertions:** Actualización de la suite de pruebas `httpXX.json` para validar estructuras anidadas y
  jerárquicas en las respuestas de la API.

### Changed

- **Java 21 Syntax Refinement:** Aplicación estricta de `final var` para variables locales y parámetros de método en
  toda la capa de servicio y controladores.
- **DTO Structural Refactoring:** Migración de `ProductResponse` hacia un diseño de records estáticos anidados para
  mejorar la cohesión de la información del vendedor y envío.
- **Database Initialization Flow:** Configuración de `defer-datasource-initialization` para sincronizar la ejecución de
  scripts SQL con el ciclo de vida de Hibernate.

### Fixed

- **Integration Test Payloads:** Corrección de errores 400 en `SecurityIntegrationTest` mediante la actualización de los
  cuerpos JSON de prueba con los nuevos campos obligatorios.
- **Service Layer Visibilidad:** Ajuste de modificadores de acceso en Records para permitir el mapeo correcto entre
  paquetes de DTO y Servicio.

### Removed

- **Legacy Seeder:** Eliminación de `ProductDataSeeder.java` en favor del estándar `data.sql` basado en archivos de
  recursos.

## [0.10.0] - 2026-01-21

### Added

- **Contextual User Logging (MDC):** Implementación de Mapped Diagnostic Context para inyectar automáticamente el nombre
  del usuario autenticado en cada línea de log.
- **Strategic Service Logging:** Cobertura total de logs en inglés técnico para el ciclo de vida de productos (Create,
  Update, Delete, Get, Pagination) siguiendo niveles INFO, DEBUG y WARN.
- **Smart Log Patterns:** Configuración de un patrón de consola dinámico que muestra `[user]` o `[system]` (fallback)
  con resaltado de sintaxis en el perfil de desarrollo.
- **Security Audit Logs:** Registro de intentos de autenticación exitosos y fallidos en el `AuthController` y filtros de
  seguridad.

### Changed

- **Logback Refactoring:** Actualización de `logback-spring.xml` para soportar tanto logs estructurados JSON para
  contenedores como logs legibles para humanos en local.
- **Filter Optimization:** Refactorización del `JwtAuthenticationFilter` mediante métodos privados y Guard Clauses para
  manejar la limpieza del contexto del hilo (`MDC.clear()`) de forma segura.

### Fixed

- **Thread Context Leak:** Garantizada la limpieza de los datos del usuario en los hilos de ejecución mediante bloques
  `try-finally` en los filtros, evitando que la información se filtre entre peticiones.

## [0.9.0] - 2026-01-21

### Added

- **Multi-Stage Docker Architecture:** Implementación de un `Dockerfile` optimizado con etapas diferenciadas para
  Desarrollo (Hot-reload), Construcción (Maven) y Producción (JRE Alpine).
- **Advanced Observability:** - Configuración de **Structured Logging** en formato JSON mediante Logback y Logstash
  Encoder.
    - Implementación de **Métricas de Negocio** personalizadas con Micrometer (`api.products.created`).
- **Docker Compose Orchestration:** Creación de entornos segregados (`docker-compose.yaml` para dev y
  `docker-compose.test.yml` para integración).
- **Production-Ready Security:** Refuerzo de la entropía de la clave JWT (256 bits) para cumplir con el estándar RFC
    7518.

### Changed

- **Runtime Optimization:** Migración a imágenes base `eclipse-temurin:21-jre-alpine` reduciendo el tamaño de la imagen
  final en un 60%.
- **Container Security:** Implementación de ejecución mediante usuario no-root (`spring:spring`) dentro del contenedor.
- **Developer Experience:** Configuración de volúmenes para persistencia del `maven-cache` y sincronización de código en
  tiempo real.

### Fixed

- **WeakKeyException:** Solucionado el error de arranque del contenedor debido a claves JWT de longitud insuficiente.
- **Docker Path Mismatch:** Corrección de la ejecución del comando `mvn spring-boot:run` para entornos con volúmenes
  montados.

## [0.8.0] - 2026-01-21

### Added

- **JWT Security Architecture:** Implementación de seguridad sin estado (Stateless) mediante tokens JWT.
- **Bearer Authentication in Swagger:** Configuración de `SecurityScheme` en OpenAPI 3.0 para permitir pruebas
  autenticadas desde Swagger UI.
- **Standardized Error RFC 7807:** Adopción total del estándar internacional `ProblemDetail` para todas las respuestas
  de error (400, 401, 404, 429).
- **Type-Safe Configuration:** Implementación de `JwtProperties` utilizando Java Records y `@ConfigurationProperties`.

### Changed

- **Java 21 Refactoring:** Aplicación de estándares modernos: uso de `var`, parámetros `final`, y Records para DTOs e
  infraestructura.
- **Unified Exception Handling:** Sincronización de errores de Spring Security y Rate Limiting con el
  `GlobalExceptionHandler`.
- **Logic Cleanup:** Implementación de "Guard Clauses" en filtros y servicios para mejorar la legibilidad.

### Fixed

- **Rate Limiting Consistency:** Corrección del formato de salida del `RateLimitingFilter` para alinearse con el esquema
  global RFC 7807.
- **Security Integration Tests:** Actualización de las aserciones de `MockMvc` para validar los nuevos paths de JSON (
  `$.detail` en lugar de `$.message`).

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

[unreleased]: https://github.com/Gabrieldrc/example/compare/1.0.0...HEAD

[1.0.0]: https://github.com/Gabrieldrc/example/compare/0.11.0...1.0.0

[0.11.0]: https://github.com/Gabrieldrc/example/compare/0.10.0...0.11.0

[0.10.0]: https://github.com/Gabrieldrc/example/compare/0.9.0...0.10.0

[0.9.0]: https://github.com/Gabrieldrc/example/compare/0.8.0...0.9.0

[0.8.0]: https://github.com/Gabrieldrc/example/compare/0.7.0...0.8.0

[0.7.0]: https://github.com/Gabrieldrc/example/compare/0.6.0...0.7.0

[0.6.0]: https://github.com/Gabrieldrc/example/compare/0.5.0...0.6.0

[0.5.0]: https://github.com/Gabrieldrc/example/compare/0.4.0...0.5.0

[0.4.0]: https://github.com/Gabrieldrc/example/compare/0.3.0...0.4.0

[0.3.0]: https://github.com/Gabrieldrc/example/compare/0.2.0...0.3.0

[0.2.0]: https://github.com/Gabrieldrc/example/compare/0.1.0...0.2.0

[0.1.0]: https://github.com/Gabrieldrc/example/releases/tag/v0.1.0
