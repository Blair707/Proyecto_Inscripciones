# API Gateway — Plataforma Educativa

Spring Cloud Gateway que autentica requests mediante **AWS Cognito (IdaaS)** y enruta hacia los microservicios.

## Arquitectura

```
Cliente → [API Gateway :8090] → valida JWT (Cognito) → [MS Guías :8080]
```

## Flujo de autenticación

1. El cliente obtiene un **access token** desde AWS Cognito (User Pool `us-east-1_iguEGAmGK`).
2. Incluye el token en el header: `Authorization: Bearer <token>`
3. El Gateway valida la firma RSA contra el JWKS público de Cognito.
4. Si es válido, propaga la petición al microservicio con `TokenRelay` y cabeceras de identidad (`X-User-Sub`, `X-User-Username`).
5. Si es inválido → **401 Unauthorized** (el microservicio nunca recibe la petición).

## Rutas registradas

| Ruta Gateway          | Microservicio destino          | Auth requerida |
|-----------------------|-------------------------------|---------------|
| `GET/POST /api/guias/**` | guias-despacho `:8080`      | ✅ JWT Cognito |
| `PUT /api/guias/{id}` | guias-despacho `:8080`        | ✅ JWT Cognito |
| `DELETE /api/guias/{id}` | guias-despacho `:8080`     | ✅ JWT Cognito |
| `GET /actuator/health`| Gateway (local)               | ❌ Público     |
| `GET /services/guias/health` | guias-despacho health   | ❌ Público     |

## Variables de entorno

| Variable              | Descripción                        | Default                   |
|-----------------------|------------------------------------|---------------------------|
| `COGNITO_USER_POOL_ID`| ID del User Pool de Cognito        | `us-east-1_iguEGAmGK`     |
| `GUIAS_SERVICE_URL`   | URL del microservicio guías        | `http://localhost:8080`   |

## Ejecución local

```bash
# Con microservicio en localhost:8080
mvn spring-boot:run

# Con variables de entorno
GUIAS_SERVICE_URL=http://guias-service:8080 mvn spring-boot:run
```

## Docker Compose (recomendado)

```yaml
version: '3.8'
services:
  api-gateway:
    build: ./api-gateway
    ports:
      - "8090:8090"
    environment:
      - COGNITO_USER_POOL_ID=us-east-1_iguEGAmGK
      - GUIAS_SERVICE_URL=http://guias-despacho:8080
    depends_on:
      - guias-despacho

  guias-despacho:
    build: ./guias-despacho
    ports:
      - "8080:8080"
    environment:
      - DB_HOST=mysql
      - DB_NAME=guias_db
      - COGNITO_USER_POOL_ID=us-east-1_iguEGAmGK
```
