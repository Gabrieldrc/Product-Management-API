# Usamos Eclipse Temurin como base oficial recomendada en 2026
FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

# Optimizamos el cache de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Usuario no root por seguridad
RUN useradd -m meliuser && chown -R meliuser /app
USER meliuser

# Puertos: 8080 (App), 5005 (Debugger)
EXPOSE 8080
EXPOSE 5005

# Por defecto corre la app, pero los comandos del compose pueden sobrescribir esto
CMD ["mvn", "spring-boot:run"]