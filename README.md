# Product Management API 🚀

Microservicio profesional para la gestión de productos, desarrollado con **Java 21** y **Spring Boot 3**.

---

## 🛠️ Instrucciones de Ejecución

Este proyecto utiliza Docker y Docker Compose para una configuración y ejecución sencillas.

### 1. Desarrollo (Modo Hot-Reload & Debug)
Para levantar el entorno de desarrollo con recarga en caliente y puerto de depuración 5005, ejecuta:

```bash
docker compose up --build
```
La aplicación utiliza una base de datos en memoria H2 para simular la persistencia de datos. Los datos iniciales se cargan automáticamente desde `src/main/resources/data.sql` al iniciar la aplicación.

### 2. Ejecución de Tests
Para correr la suite completa de pruebas unitarias y de integración, puedes usar Maven directamente o Docker Compose:

```bash
# Ejecutar tests con Maven (requiere JDK 21 instalado localmente)
mvn test

# Ejecutar tests de integración con Docker Compose
docker compose -f docker-compose.test.yml up --build
```

### 3. Integración y Producción (Build Optimizado)
Para levantar la versión final empaquetada en una imagen ligera (Alpine) sin herramientas de compilación:

```bash
docker compose -f docker-compose.test.yml up --build
```

---

## 🔐 Guía de Autenticación Rápida

Para interactuar con los endpoints protegidos, sigue estos pasos en tu terminal:

**Paso 1: Login para obtener el Token**
```bash
curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "admin", "password": "admin123"}'
```

**Paso 2: Ejemplo de uso del Token (por ejemplo, para crear un producto)**
```bash
curl -X POST http://localhost:8080/products \
     -H "Authorization: Bearer <TU_TOKEN_AQUI>" \
     -H "Content-Type: application/json" \
     -d '{
       "title": "iPhone 15 Pro Max",
       "description": "The latest iPhone with titanium body",
       "price": 1250.5,
       "stock": 50,
       "condition": "NEW",
       "imageUrls": [
         "https://cdn.example.com/p1.jpg"
       ],
       "sellerName": "Apple Official Store",
       "sellerRating": 4.8,
       "shippingCost": 15,
       "estimatedDelivery": "Arrives by Friday"
     }'
```

---

## 📊 Endpoints de Monitoreo y Observabilidad

Accede a estos recursos desde tu navegador o mediante `curl` para monitorear el estado y las métricas de la aplicación:

```bash
# Verificar salud del sistema
curl http://localhost:8080/actuator/health

# Ver métricas disponibles
curl http://localhost:8080/actuator/metrics
```

---

## 📄 Documentación Interactiva de la API (Swagger UI)

La documentación completa y las capacidades de prueba de la API están disponibles a través de Swagger UI. Aquí podrás explorar todos los endpoints, sus modelos de datos, y probar las solicitudes directamente desde tu navegador.

**URL de Swagger UI:** [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)

---

## 🏛️ Decisiones Arquitectónicas y Buenas Prácticas

Durante el desarrollo de este microservicio, se han implementado las siguientes decisiones y buenas prácticas:

*   **Tecnologías Base:** Desarrollado con **Java 21** y **Spring Boot 3.2.5**, aprovechando las últimas características de la plataforma.
*   **Persistencia de Datos:** Se utiliza una base de datos en memoria **H2** para simular el inventario, lo que permite un entorno de desarrollo rápido y ligero. Los datos iniciales se cargan automáticamente mediante [`src/main/resources/data.sql`](src/main/resources/data.sql) al inicio de la aplicación.
*   **Manejo de Errores:** La API implementa un manejo de errores centralizado utilizando `@RestControllerAdvice` y `ProblemDetail` (RFC 7807), asegurando respuestas de error consistentes y detalladas (e.g., `404 Not Found` para recursos no encontrados).
*   **Documentación de la API:** Integración con **SpringDoc OpenAPI** para generar automáticamente la documentación interactiva de la API (Swagger UI), accesible en [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html).
*   **Seguridad:** Implementación de seguridad sin estado (Stateless) mediante **JSON Web Tokens (JWT)**, con autenticación `Bearer` configurada en Swagger UI para facilitar las pruebas.
*   **Rate Limiting:** Uso de la librería **Bucket4j** para controlar la tasa de solicitudes a la API, protegiendo el servicio contra abusos.
*   **Observabilidad:**
    *   **Actuator:** Endpoints de monitoreo (`/health`, `/metrics`, `/info`) para supervisar el estado y las métricas de la aplicación.
    *   **Structured Logging:** Configuración de **Logback** con **Logstash Encoder** para generar logs estructurados en formato JSON, optimizados para entornos de contenedores.
*   **Estándares de Codificación:** Adherencia estricta a los estándares de codificación definidos en [`coding-standards.md`](coding-standards.md), incluyendo:
    *   Uso de `records` para DTOs y configuraciones.
    *   Inyección de dependencias por constructor.
    *   Uso de `final` para parámetros y variables locales (`var`).
    *   Manejo de fechas con `java.time.Instant`.
    *   Uso de "Guard Clauses" para mejorar la legibilidad.
    *   Código y comentarios exclusivamente en inglés.
*   **Testing:** Cobertura de pruebas unitarias y de integración para las capas de controlador y servicio, asegurando la robustez de la API.

---

## 📂 Requisitos del Sistema
*   Docker & Docker Compose
*   JDK 21 (Solo si corres fuera de Docker)
