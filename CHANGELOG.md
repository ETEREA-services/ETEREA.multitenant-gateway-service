# Changelog

Todos los cambios notables en este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.0.0] - 2026-10-31
### Added
- feat(security): `SecurityConfig` configura el gateway como OAuth2 Resource Server y valida JWT HS256 (issuer y claim `authorities`).
- feat(security): dependencia `spring-boot-starter-oauth2-resource-server` en `pom.xml`.
- test: `src/test/resources/bootstrap.yml` con Consul deshabilitado y un secreto JWT de prueba.
- docs: `AGENTS.md` con el rol, la configuración y las reglas de cambio del servicio.

### Changed
- feat(security)!: **BREAKING** — todas las rutas requieren `Authorization: Bearer <JWT>`; solo `/api/brain/auth/login`, `/api/brain/auth/refresh`, `/api/brain/auth/logout`, `/actuator/health` y `/actuator/info` quedan públicos.
- feat(routing)!: **BREAKING** — la ruta `multitenant-core-service` reemplaza `/api/v1/auth/**` por `/api/brain/**`.
- feat(config)!: **BREAKING** — se agregan `app.jwt.secret` (`APP_JWT_SECRET`, mínimo 32 caracteres) y `app.jwt.issuer` (`APP_JWT_ISSUER`); el arranque falla si el secreto no alcanza el mínimo.
- feat(actuator): `management.endpoint.health.show-details` pasa de `always` a `when-authorized`.
- fix(logging): `LoggingGlobalFilter` redacta `Authorization`, `Cookie`, `Set-Cookie` y `Proxy-Authorization` en los logs de request y response.
- build(deps): Actualización de Spring Boot de 4.1.0 a 4.1.2.
- build(deps): Actualización de Spring Cloud a 2025.1.4.
- build(deps): Actualización de JaCoCo de 0.8.13 a 0.8.16.
- docs(pipeline): el visor de documentación escapa el contenido de los diagramas `.mmd` antes de inyectarlo con `innerHTML`.

### Removed
- feat(routing)!: **BREAKING** — se elimina la ruta `/api/v1/auth/**`.

## [2.2.1] - 2026-06-28
### Changed
- build(deps): Actualización de Spring Boot de 4.0.6 a 4.1.0
- build(deps): Actualización de Spring Cloud de 2025.1.0 a 2025.1.3

## [2.2.0] - 2026-05-27
### Changed
- build(deps): Actualización de Spring Boot de 3.5.6 a 4.0.6
- build(deps): Actualización de Java de 24 a 25
- build(deps): Actualización de Spring Cloud de 2025.0.0 a 2025.1.0
- ci(actions): Actualización de GitHub Actions a versiones superiores (checkout v6, setup-java v5, cache v5, deploy-pages v5)
- ci(docker): Actualización de imágenes base Docker a JDK 25
- docs(pipeline): Corrección de ruta de salida de documentación de `public/` a `docs/`

## [2.1.0] - 2025-09-21
### Added
- feat(logging): Added LoggingGlobalFilter for comprehensive request and response logging
- feat(routing): Added new route for lista-precio-proxy service

### Changed
- build(deps): Updated Spring Boot starter parent from 3.5.4 to 3.5.6

## [2.0.1] - 2025-08-05
### Changed
- Actualización de la dependencia `spring-boot-starter-parent` de la versión `3.5.3` a `3.5.4` en `pom.xml`.
- Implementación de Dockerfile multi-etapa para optimizar el tamaño de la imagen y mejorar la seguridad con usuario no privilegiado.

### Removed
- Eliminación de `Dockerfile.local`.
- Eliminación de `pages.png`.

## [2.0.0] - 2025-07-24
### Changed
- Se reemplazó el sistema de descubrimiento de servicios: **de Eureka a Consul**.
- Actualización de la configuración en `bootstrap.yml` para soportar Consul.
- Cambio del nombre del proyecto a "ETEREA.gateway-service" en `pom.xml`.

### Removed
- Eliminada la dependencia de `spring-cloud-starter-netflix-eureka-client`.
- Eliminada la configuración de Eureka en los archivos de configuración.

### Notes
- Este cambio es incompatible con versiones anteriores que dependían de Eureka para el descubrimiento de servicios.

## [1.2.0] - 2025-07-21
### Added
- Refactorización del workflow de CI/CD para construcción de imágenes JVM, incluyendo login a Docker Hub y etiquetado semántico.

## [1.1.2] - 2025-07-06
### Fixed
- fix(docs): reparar la estructura de index.html en el workflow
- fix(docs): corregir la generación de data.js en el workflow

## [1.1.1] - 2025-07-04
### Changed
- build(deps): update Spring Boot/Cloud and gateway config

## [1.1.0] - 2025-06-30
### Added
- feat(monitoring): implementar sistema de métricas avanzadas
- feat: actualiza dependencias y mejora infraestructura

## [1.0.0] - 2024-03-24
### Added
- Actualización de dependencias:
  - Spring Boot a 3.4.5
  - Java a versión 24
- Implementación de documentación automática:
  - Generación de documentación técnica
  - Wiki del proyecto
  - GitHub Pages para documentación
- Scripts de automatización:
  - `fetch_github_data.py` para obtener datos del repositorio
  - `generate_docs.py` para generar documentación
  - `generate_wiki.py` para mantener la wiki actualizada

### Changed
- Actualización de Dockerfiles para usar Java 24
- Optimización del workflow de Maven
- Mejoras en la infraestructura de CI/CD

## [0.9.0] - 2024-12-16
### Added
- Endpoints de Actuator para monitoreo
- Centralización del nombre del servicio

## [0.8.0] - 2024-12-05
### Changed
- Actualización de versiones de dependencias

## [0.7.0] - 2024-11-15
### Changed
- Actualización de versiones de dependencias

## [0.6.0] - 2024-10-13
### Added
- Nueva ruta para Programa Día Service (`/api/programa-dia/**`)

## [0.5.0] - 2024-09-29
### Added
- Nueva ruta para Stock Service (`/api/stock/**`)

## [0.4.0] - 2024-09-22
### Changed
- Actualización de versiones de dependencias

## [0.3.0] - 2024-09-11
### Changed
- Modificación del puerto de servicio

## [0.2.0] - 2024-09-08
### Changed
- Optimización de Dockerfile para reducir tamaño de imagen

## [0.1.0] - 2024-08-31
### Changed
- Actualización de versiones de dependencias

## [0.0.3] - 2024-08-13
### Added
- Badge de CI en README
- Mejoras en GitHub Actions

## [0.0.2] - 2024-07-09
### Added
- Configuración inicial de GitHub Actions
- Primera implementación del servicio

## [0.0.1] - 2024-07-06
### Added
- Commit inicial del proyecto
- Configuración básica de Maven
- Estructura inicial del proyecto

[3.0.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v2.2.1...v3.0.0
[2.2.1]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v2.2.0...v2.2.1
[2.2.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v2.1.0...v2.2.0
[2.1.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v2.0.1...v2.1.0
[2.0.1]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v2.0.0...v2.0.1
[2.0.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v1.2.0...v2.0.0
[1.2.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v1.1.2...v1.2.0
[1.1.2]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v1.1.1...v1.1.2
[1.1.1]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v0.9.0...v1.0.0
[0.9.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v0.8.0...v0.9.0
[0.8.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v0.7.0...v0.8.0
[0.7.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v0.6.0...v0.7.0
[0.6.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v0.5.0...v0.6.0
[0.5.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v0.4.0...v0.5.0
[0.4.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v0.3.0...v0.4.0
[0.3.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v0.0.3...v0.1.0
[0.0.3]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v0.0.2...v0.0.3
[0.0.2]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/compare/v0.0.1...v0.0.2
[0.0.1]: https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/releases/tag/v0.0.1