# Product Management API 🚀

Microservicio profesional para la gestión de productos, desarrollado con **Java 21** y **Spring Boot 3**.

---

## 🛠️ Instrucciones de Ejecución (Bash)

### 1. Desarrollo (Modo Hot-Reload & Debug)
Ejecuta el entorno de desarrollo con volúmenes sincronizados y puerto de debug 5005.

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

## 📂 Requisitos del Sistema
* Docker & Docker Compose
* JDK 21 (Solo si corres fuera de Docker)