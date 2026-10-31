# ETEREA.multitenant-gateway-service

[![ETEREA.multitenant-gateway-service Build JVM Image](https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/actions/workflows/maven.yml)
[![Java Version](https://img.shields.io/badge/Java-25-blue.svg)](https://www.oracle.com/java/technologies/downloads/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-green.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.1.4-blue.svg)](https://spring.io/projects/spring-cloud)
[![Spring Cloud Gateway](https://img.shields.io/badge/Spring%20Cloud%20Gateway-WebFlux-green.svg)](https://spring.io/projects/spring-cloud-gateway)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-OAuth2%20JWT%20HS256-green.svg)](https://spring.io/projects/spring-security)
[![Consul](https://img.shields.io/badge/Consul-Service%20Discovery-blue.svg)](https://developer.hashicorp.com/consul)
[![Maven](https://img.shields.io/badge/Maven-Build-blue.svg)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Multi--stage-blue.svg)](https://www.docker.com/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5-blue.svg)](https://junit.org/junit5/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=ETEREA-services_ETEREA.multitenant-gateway-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ETEREA-services_ETEREA.multitenant-gateway-service)
[![SonarCloud Bugs](https://sonarcloud.io/api/project_badges/measure?project=ETEREA-services_ETEREA.multitenant-gateway-service&metric=bugs)](https://sonarcloud.io/summary/new_code?id=ETEREA-services_ETEREA.multitenant-gateway-service)
[![SonarCloud Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=ETEREA-services_ETEREA.multitenant-gateway-service&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=ETEREA-services_ETEREA.multitenant-gateway-service)
[![SonarCloud Code Smells](https://sonarcloud.io/api/project_badges/measure?project=ETEREA-services_ETEREA.multitenant-gateway-service&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=ETEREA-services_ETEREA.multitenant-gateway-service)
[![SonarCloud Duplication](https://sonarcloud.io/api/project_badges/measure?project=ETEREA-services_ETEREA.multitenant-gateway-service&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=ETEREA-services_ETEREA.multitenant-gateway-service)
[![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=ETEREA-services_ETEREA.multitenant-gateway-service&metric=coverage)](https://sonarcloud.io/summary/new_code?id=ETEREA-services_ETEREA.multitenant-gateway-service)

## Description
ETEREA.multitenant-gateway-service is a Spring Cloud Gateway service that acts as the entry point for all microservices in the ETEREA ecosystem. It provides routing, load balancing, and security features for the distributed system.

## Features
- Dynamic service routing
- Load balancing with Spring Cloud LoadBalancer
- **Service discovery with Consul**
- **JWT authentication and authorization** with Spring Security OAuth2 Resource Server (HS256)
- Health monitoring with Spring Boot Actuator
- Request/response logging with LoggingGlobalFilter
- Caffeine cache for optimized load balancing
- SonarCloud integration for code quality analysis

## Prerequisites
- Java 25
- Maven 3.x
- Docker (optional)
- A JWT secret shared with `multitenant-core-service` (at least 32 characters)

## Getting Started

### Local Development
1. Clone the repository
```bash
git clone https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service.git
```

2. Build the project
```bash
mvn clean install
```

3. Run the service (the JWT secret is mandatory, the service fails to start without it)
```bash
APP_JWT_SECRET='cambiar-por-un-secreto-de-32-bytes-minimo' \
APP_JWT_ISSUER=eterea.multitenant \
mvn spring-boot:run
```

### Docker
Build and run with Docker:
```bash
docker build -t eterea-multitenant-gateway-service:3.0.0 .
docker run -p 8080:8080 \
  -e APP_JWT_SECRET='cambiar-por-un-secreto-de-32-bytes-minimo' \
  -e APP_JWT_ISSUER=eterea.multitenant \
  eterea-multitenant-gateway-service:3.0.0
```

## Configuration
The service can be configured through `bootstrap.yml` or environment variables. Key configurations include:
- Server port (`APP_PORT`, default `8080`)
- **Consul service discovery settings** (`APP_CONSUL_HOST`, `APP_CONSUL_PORT`)
- Route definitions
- Load balancer settings
- JWT validation: `app.jwt.secret` / `APP_JWT_SECRET` (shared HS256 secret, minimum 32 characters, no default) and `app.jwt.issuer` / `APP_JWT_ISSUER` (default `eterea.multitenant`)

### Security
All routes require a valid `Authorization: Bearer <JWT>` header, except:
- `/api/brain/auth/login`
- `/api/brain/auth/refresh`
- `/api/brain/auth/logout`
- `/actuator/health`
- `/actuator/info`

`/actuator/**` requires the `ADMIN` authority and `OPTIONS` requests are permitted. The token's `authorities` claim is mapped to the security authorities.

## Health Endpoints
The service exposes health endpoints via Spring Boot Actuator:
- `/actuator/health` - Service health status (public)
- `/actuator/info` - Service information (public)
- `/actuator/metrics` - Service metrics (requires the `ADMIN` authority)

Other `/actuator/**` endpoints require the `ADMIN` authority.

## Documentation
- [API Documentation](https://eterea-services.github.io/ETEREA.multitenant-gateway-service/)
- [Technical Wiki](https://github.com/ETEREA-services/ETEREA.multitenant-gateway-service/wiki)

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License
This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Recent Changes
- **v3.0.0 Release**
- Breaking: Added JWT validation with Spring Security OAuth2 Resource Server (HS256). All routes now require `Authorization: Bearer <JWT>`, except login, refresh, logout, health and info.
- Breaking: Replaced route `/api/v1/auth/**` with `/api/brain/**` in `multitenant-core-service`.
- Breaking: Added `APP_JWT_SECRET` (min. 32 characters) and `APP_JWT_ISSUER` configuration; startup fails without a valid secret.
- Changed: `LoggingGlobalFilter` redacts credential headers in logs.
- Changed: Spring Boot 4.1.0 to 4.1.2 and Spring Cloud to 2025.1.4.
- Changed: JaCoCo 0.8.13 to 0.8.16.
- **v2.2.1 Release**
- Changed: Actualización de Spring Boot de 4.0.6 a 4.1.0
- Changed: Actualización de Spring Cloud de 2025.1.0 a 2025.1.3
- **v2.2.0 Release**
- Changed: Actualización de Spring Boot de 3.5.6 a 4.0.6
- Changed: Actualización de Java de 24 a 25
- Changed: Actualización de Spring Cloud de 2025.0.0 a 2025.1.0
- Changed: Actualización de GitHub Actions a versiones superiores
- **v2.1.0 Release**
- Added: Request/response logging with LoggingGlobalFilter
- Added: New route for lista-precio-proxy service
- Changed: Updated Spring Boot starter parent from 3.5.4 to 3.5.6
- **v2.0.1 Release**
- Changed: Actualización de la dependencia `spring-boot-starter-parent` de la versión `3.5.3` a `3.5.4` en `pom.xml`.
- **v2.0.0 Release**
- Changed: Se reemplazó el sistema de descubrimiento de servicios de Eureka a Consul.
- Changed: Actualización de la configuración en `bootstrap.yml` para soportar Consul.
- Changed: Cambio del nombre del proyecto a "ETEREA.gateway-service" en `pom.xml`.
- Removed: Eliminada la dependencia de `spring-cloud-starter-netflix-eureka-client`.
- Removed: Eliminada la configuración de Eureka en los archivos de configuración.
- **v1.2.0 Release**
- Added: Integración con SonarCloud para análisis de calidad de código.
- Changed: Refactorización del workflow de CI/CD para construcción de imágenes JVM, incluyendo login a Docker Hub y etiquetado semántico.
- Changed: Implementación de Dockerfile multi-etapa para optimizar el tamaño de la imagen y mejorar la seguridad con usuario no privilegiado.
- Removed: Eliminación de los scripts del Maven Wrapper (`mvnw`, `mvnw.cmd`).
- Removed: Eliminación de `Dockerfile.local`.
- Removed: Eliminación de `pages.png`.

## Contact
ETEREA Services Team