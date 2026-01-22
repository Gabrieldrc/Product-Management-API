# Product Management API 🚀

Microservicio profesional para la gestión de productos, desarrollado con **Java 21** y **Spring Boot 3**.

---

## 🛠️ Instrucciones de Ejecución (Bash)

### 1. Desarrollo (Modo Hot-Reload & Debug)
Ejecuta el entorno de desarrollo con volúmenes sincronizados y puerto de debug 5005.
La aplicación utiliza una base de datos en memoria H2 para simular la persistencia de datos. Los datos iniciales se cargan automáticamente desde `src/main/resources/data.sql` al iniciar la aplicación.

```bash
docker compose up --build
```

### 2. Ejecución de Tests
Corre la suite completa de pruebas unitarias y de integración.

```bash
mvn test
```

### 3. Integración y Producción (Build Optimizado)
Levanta la versión final empaquetada en una imagen ligera (Alpine) sin herramientas de compilación.

```bash
docker compose -f docker-compose.test.yml up --build
```

---

## 🔐 Guía de Autenticación Rápida

Para los endpoints protegidos, sigue estos pasos en tu terminal:

**Paso 1: Login para obtener el Token**
```bash
curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "admin", "password": "admin123"}'
```

**Paso 2: Ejemplo de creación de producto (usando el token obtenido)**
```bash
curl -X POST http://localhost:8080/products \
     -H "Authorization: Bearer <TU_TOKEN_AQUI>" \
     -H "Content-Type: application/json" \
     -d '{"title":"Nuevo Producto","price":99.99,"stock":10,"condition":"NEW","imageUrls":[]}'
```

---

## 📊 Endpoints de Monitoreo

Accede a estos recursos desde tu navegador o mediante `curl`:

```bash
# Verificar salud del sistema
curl http://localhost:8080/actuator/health

# Ver métricas disponibles
curl http://localhost:8080/actuator/metrics

# Documentación interactiva (Swagger)
# URL: http://localhost:8080/swagger-ui/index.html
```

---

## 🔍 Item Detail API

El endpoint principal para obtener los detalles de un ítem es el siguiente:

```
GET /products/{id}
```

Este endpoint devuelve los detalles completos de un producto, incluyendo su título, precio, stock, condición e URLs de imágenes. La implementación reutiliza la lógica existente en `ProductController` y `ProductService`, asegurando eficiencia y consistencia.

---

## 🏛️ Decisiones Arquitectónicas y Buenas Prácticas

Durante el desarrollo, se han seguido las siguientes decisiones y buenas prácticas:

*   **Persistencia de Datos:** Se utiliza una base de datos en memoria H2 para simular el inventario, lo que permite un entorno de desarrollo rápido y ligero sin necesidad de una base de datos externa. Los datos iniciales se cargan mediante `data.sql`.
*   **Manejo de Errores:** La API implementa un manejo de errores centralizado utilizando `@RestControllerAdvice` y `ProblemDetail` (RFC 7807), asegurando respuestas de error consistentes y detalladas, como `404 Not Found` para productos no encontrados.
*   **Documentación:** Se utilizan anotaciones de OpenAPI (`@Tag`, `@Operation`, `@ApiResponses`) y Javadoc para documentar los endpoints y la lógica de negocio, facilitando la comprensión y el uso de la API.
*   **Testing:** La funcionalidad del API de detalle de ítem está cubierta por los tests unitarios existentes en las capas de controlador y servicio, verificando tanto los casos de éxito como los de error.
*   **Estándares de Codificación:** Se ha adherido estrictamente a los estándares de codificación definidos en `coding-standards.md`, incluyendo el uso de `records` para DTOs, inyección de dependencias por constructor, uso de `final` para parámetros y variables, y `java.time.Instant` para el manejo de fechas.

---

## 📂 Requisitos del Sistema
*   Docker & Docker Compose
*   JDK 21 (Solo si corres fuera de Docker)
