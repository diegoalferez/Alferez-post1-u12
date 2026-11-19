# Post-contenido — Unidad 12: Despliegue y CI/CD

## Descripción

Repositorio del laboratorio de la Unidad 12 de Programación Web.
Contiene una aplicación Spring Boot (entidad JPA `Producto` y endpoint
REST `/api/productos`) dockerizada con un Dockerfile multi-stage y
perfiles dev/prod (Parte 1), que se extenderá con un pipeline de
integración continua con GitHub Actions y monitoreo con Spring Boot
Actuator (Parte 2).

## Parte 1 — Dockerización con Perfiles y Variables de Entorno

El Dockerfile multi-stage compila el proyecto con Maven en una etapa JDK
y ejecuta el JAR resultante en una etapa JRE Alpine con un usuario no
root. La configuración sensible se externaliza en `application.properties`
con placeholders `${VAR:default}`: sin variables de entorno, la aplicación
arranca con el perfil `dev` (H2 en memoria); con
`SPRING_PROFILES_ACTIVE=prod` y las variables `DATASOURCE_URL`,
`DATASOURCE_USER` y `DATASOURCE_PASSWORD`, arranca con la configuración de
producción (PostgreSQL).

## Decisiones de diseño

1. **Placeholders con valor por defecto (`${VAR:default}`) en vez de
   placeholders estrictos:** permite arrancar sin fricción en local y en
   CI, y deja los valores reales para producción.
2. **`application-dev.properties` y `application-prod.properties` como
   archivos separados** en vez de un único archivo con bloques
   condicionales: hace explícito qué configuración corresponde a cada
   ambiente y reduce el riesgo de editar producción por error.
3. **Imagen final `eclipse-temurin:21-jre-alpine` en vez de distroless:**
   reduce el tamaño de la imagen sin sacrificar la posibilidad de depurar
   el contenedor con `docker exec`.

## Cómo compilar y ejecutar

1. Clonar el repositorio: `git clone https://github.com/diegoalferez/Alferez-post1-u12.git`
2. Construir la imagen: `docker build -t mi-spring-app:local .`
3. Ejecutar con los valores por defecto (perfil dev):
   `docker run -p 8080:8080 mi-spring-app:local`
4. Probar el endpoint: `curl http://localhost:8080/api/productos`
5. Ejecutar con perfil prod (requiere un PostgreSQL accesible):
   `docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod -e DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/appdb -e DATASOURCE_USER=appuser -e DATASOURCE_PASSWORD=apppass mi-spring-app:local`
