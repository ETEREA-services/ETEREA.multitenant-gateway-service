# AGENTS.md · ETEREA.multitenant-gateway-service

## Rol del servicio

Spring Cloud Gateway reactivo que es el punto de entrada de la plataforma
multitenant. Descubre servicios por Consul, enruta con `lb://`, aplica un filtro
global de logging y valida el JWT HS256 en el borde. **No contiene lógica de
negocio ni emite tokens.**

## Stack

- Java 25
- Spring Boot 4.1.2
- Spring Cloud 2025.1.4
- Spring Cloud Gateway (WebFlux, reactivo)
- Spring Security: OAuth2 Resource Server + validación JWT HS256
- Consul Discovery
- Maven (no hay `mvnw` en el repo)

## Comandos

```bash
# Compilar y testear
mvn -B verify

# Empaquetar sin tests
mvn -DskipTests package

# Ejecutar en local (requiere Consul accesible)
APP_PORT=8080 \
APP_CONSUL_HOST=localhost \
APP_CONSUL_PORT=8500 \
APP_JWT_SECRET='cambiar-por-un-secreto-de-32-bytes-minimo' \
APP_JWT_ISSUER=eterea.multitenant \
mvn spring-boot:run

# Docker
docker build -t eterea/multitenant-gateway-service .
docker run --rm -p 9050:9050 \
  -e APP_PORT=9050 \
  -e APP_CONSUL_HOST=consul-service \
  -e APP_CONSUL_PORT=8500 \
  eterea/multitenant-gateway-service
```

## Configuración

La configuración efectiva vive en `src/main/resources/bootstrap.yml`:

- `app.port` / `APP_PORT`: puerto HTTP (default `8080`).
- `app.consul.host` / `APP_CONSUL_HOST`: host de Consul.
- `app.consul.port` / `APP_CONSUL_PORT`: puerto de Consul.
- `app.logging` / `APP_LOGGING`: nivel de logging.
- `app.jwt.secret` / `APP_JWT_SECRET`: secreto HS256 compartido con el core
  (mínimo 32 caracteres).
- `app.jwt.issuer` / `APP_JWT_ISSUER`: emisor esperado; debe coincidir con el core.
- `spring.cloud.gateway...discovery.locator.enabled=true`: permite rutas por
  nombre de servicio.
- Rutas declaradas bajo `spring.cloud.gateway.server.webflux.routes`.

### Rutas actuales

| Ruta | Destino |
|---|---|
| `/api/tenant/core/**` | `lb://multitenant-core-service` |
| `/api/unificado/core/**` | `lb://multitenant-core-service` |
| `/api/brain/**` | `lb://multitenant-core-service` |

`/api/brain/**` incluye el login/refresh/logout y los endpoints Brain. El gateway
valida el JWT para todo salvo login, refresh, logout y health/info.

No agregar rutas "por si acaso": cada ruta expone superficie de ataque y debe
responder a un contrato real del frontend o de otro servicio.

## Código

```text
src/main/java/eterea/gateway/api/rest/
├── GatewayApplication.java
└── configuration/
    ├── EtereaGatewayConfiguration.java   # @EnableDiscoveryClient
    ├── SecurityConfig.java               # JWT resource server y reglas de rutas
    └── LoggingGlobalFilter.java          # logging de request/response
```

- `SecurityConfig` valida el JWT HS256 y mapea el claim `authorities`. No volver a
  dejar las rutas abiertas.
- **No** hay CORS ni rate limiting. Para desarrollo, el frontend usa un proxy de
  dev-server; si se configura CORS, restringir orígenes por entorno.
- `LoggingGlobalFilter` registra método, URI, IP, headers (debug), ruta y estado.
  Los headers `Authorization`, `Cookie`, `Set-Cookie` y `Proxy-Authorization` se
  redactan; no loguear cuerpos con credenciales.
- El gateway es reactivo: no introducir dependencias bloqueantes de MVC/JPA.

## Reglas de cambio

1. **Rutas en YAML**, no hardcodeadas en Java, salvo filtros/transformaciones
   justificadas.
2. **Usar `lb://` y Consul**; no hardcodear IPs ni puertos de servicios.
3. **No agregar secretos** al `bootstrap.yml`.
4. **CORS:** hoy no hay configuración. Si el frontend en desarrollo llama directo
   al gateway, preferir proxy del dev-server de Angular antes que abrir CORS
   global. Si se configura CORS, restringir orígenes por entorno.
5. **Orden de filtros:** respetar `Ordered`; el logging debe seguir siendo el
   último (`LOWEST_PRECEDENCE`) salvo razón concreta.
6. **Compatibilidad de Spring Cloud:** mantener la versión administrada por el
   `pom.xml`; no fijar versiones sueltas de starters.
7. **Cambios cross-repo:** si se agrega una ruta, actualizar el `AGENTS.md` de
   `compose/` y `frontend/` si cambia el contrato.

## Verificación

```bash
mvn -B verify
```

Prueba manual mínima (con core y Consul arriba):

```bash
# Health del gateway
curl -i http://localhost:9050/actuator/health

# Ruta existente
curl -i -H 'X-TENANT-ID: <tenantId>' \
  http://localhost:9050/api/tenant/core/...

# Login común (público)
curl -i -X POST \
  -H 'Content-Type: application/json' \
  -d '{"login":"...","password":"...","app":"contable"}' \
  http://localhost:9050/api/brain/auth/login

# Endpoint protegido: sin Bearer debe devolver 401
curl -i http://localhost:9050/api/brain/core/empresa
```

Si una ruta devuelve 404:

1. Confirmar que el `Path` está en `bootstrap.yml`.
2. Confirmar que el servicio destino está registrado en Consul.
3. Confirmar que el controlador del core expone exactamente esa ruta.
4. Revisar logs del `LoggingGlobalFilter`.

## Git

- Rama actual: `main`.
- No crear ramas, commitear ni pushear sin autorización explícita.
- El repo tiene CI en `.github/workflows/maven.yml` (build + SonarCloud + imagen).

## Documentación y grafo

- `README.md`: descripción general y stack.
- `docs/diagrams/`: diagramas Mermaid de flujo y estado.
- `graphify-out/`: grafo pequeño (4 archivos Java). Para dudas simples suele
  alcanzar con leer el archivo; para impacto usar:

```bash
graphify query "¿qué rutas expone el gateway?" --budget 800
graphify explain "LoggingGlobalFilter"
```
